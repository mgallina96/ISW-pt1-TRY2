package main.model.media;

import main.model.Database;
import main.model.media.filesystem.FileSystem;
import main.utility.data.Field;
import main.utility.exceptions.MediaNotFoundException;
import main.utility.exceptions.MediaTypeNotFoundException;
import main.utility.exceptions.PathNotFoundException;
import main.view.listeners.OperatorScreenListener;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static main.GlobalParameters.MEDIA_DATABASE_FILE_PATH;

/**
 * @author Manuel Gallina
 * @since Version 2
 */
public class MediaDatabase implements Database, OperatorScreenListener {
    private FileSystem fileSystem;
    private HashMap<Integer, Media> mediaData;
    private HashMap<String, MediaType> mediaTypes;
    private Integer mediaCount;
    private boolean saveEnabled;

    public MediaDatabase(boolean saveEnabled, FileSystem fileSystem) {
        this.mediaData = new HashMap<>();
        this.mediaTypes = new HashMap<>();
        this.fileSystem = fileSystem;
        this.saveEnabled = saveEnabled;
    }

    public MediaDatabase(FileSystem fileSystem) {
        this(true, fileSystem);
    }

    @Override
    public void initDatabase() {
        try {
            load();
        } catch(IOException e) {
            this.mediaCount = 0;
        }
    }

    @Override
    public void save() {
        if(this.isSaveEnabled()) {
            try (
                    //to increase serializing speed
                    RandomAccessFile raf = new RandomAccessFile(MEDIA_DATABASE_FILE_PATH, "rw");
                    FileOutputStream fileOut = new FileOutputStream(raf.getFD());
                    ObjectOutputStream out = new ObjectOutputStream(fileOut)
            ) {
                out.writeObject(mediaData);
                out.writeObject(mediaTypes);
                out.writeObject(mediaCount);
            }
            catch(IOException ioEx) {
                ioEx.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void load() throws IOException {
        try (
                FileInputStream fileIn = new FileInputStream(MEDIA_DATABASE_FILE_PATH);
                ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            this.mediaData = (HashMap<Integer, Media>) in.readObject();
            this.mediaTypes = (HashMap<String, MediaType>) in.readObject();
            this.mediaCount = (Integer) in.readObject();
        }
        catch(ClassNotFoundException cnfEx) {
            cnfEx.printStackTrace();
        }
    }

    HashMap<Integer, Media> getMediaData() {
        return this.mediaData;
    }

    int getMediaNumber() {
        int counter = 0;
        for(Map.Entry<Integer, Media> mediaEntry : mediaData.entrySet())
            if(mediaEntry.getValue().isAvailable())
                counter++;
        return counter;
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
    }

    public void addMediaType(String name, ArrayList<Field> fields) {
        this.mediaTypes.put(name, new MediaType(name, fields));
        this.save();
    }

    @Override
    public String getCurrentUserName() {
        return null;
    }

    @Override
    public String getUserList() {
        return null;
    }

    @Override
    public void addMedia(String mediaType, ArrayList<String> values, String category) {
        this.mediaData.put(
                this.mediaCount,
                new Media(
                        this.mediaCount,
                        this.mediaTypes.get(mediaType),
                        values,
                        fileSystem.getFolder(category)
                )
        );
        mediaCount++;
        this.save();
    }

    @Override
    public void removeMedia(Integer mediaId) throws MediaNotFoundException {
        if(this.mediaData.containsKey(mediaId) && this.mediaData.get(mediaId).isAvailable()) {
            this.mediaData.get(mediaId).setAvailable(false);
            this.save();
        } else
            throw new MediaNotFoundException();
    }

    @Override
    public ArrayList<Field> getTypeValues(String mediaType) throws MediaTypeNotFoundException {
        for(String s : mediaTypes.keySet()) {
            if(s.equals(mediaType))
                return mediaTypes.get(mediaType).getFields();
        }
        throw new MediaTypeNotFoundException();
    }

    @Override
    public String getMediaList(String category) throws PathNotFoundException {
        StringBuilder stringBuilder = new StringBuilder();
        if(fileSystem.getFolderStructure().containsKey(category)) {
            for(Media media : mediaData.values())
                if(media.getCategory().getFolderPath().equals(category))
                    stringBuilder.append(media.toString());
        } else
            throw new PathNotFoundException();
        return stringBuilder.toString();
    }

    @Override
    public String getFolderTree() {
        return null;
    }

    @Override
    public void isValidPath(String path) throws PathNotFoundException {

    }
}
