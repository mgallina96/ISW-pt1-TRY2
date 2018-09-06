package main.model.media.filesystem;

import main.model.Database;
import main.utility.data.Field;
import main.utility.exceptions.MediaTypeNotFoundException;
import main.utility.exceptions.PathNotFoundException;
import main.utility.notifications.Notifications;
import main.view.listeners.OperatorScreenListener;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static main.GlobalParameters.FILESYSTEM_FILE_PATH;

/**
 * The file system for this application.
 * <p>
 * Manages the general folder structure and provides methods for checking path validity, checking path presence,
 * returning sub-folders.
 *
 * @author Manuel Gallina, Giosuè Filippini, Alessandro Polcini
 * @since Version 2
 */
public class FileSystem implements Database, OperatorScreenListener {
    private static final Folder ROOT = new Folder("root");
    private static final String ROOT_PATH = ROOT.getFolderPath();

    private HashMap<String, Folder> folderStructure;
    private String allPaths;
    private boolean saveEnabled;

    private transient Logger logger;

    public FileSystem(boolean saveEnabled) {
        this.saveEnabled = saveEnabled;
        this.folderStructure = new HashMap<>();
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    public FileSystem() {
        this(true);
    }

    @Override
    public void initDatabase() {
        try {
            loadFileSystem();
        } catch(IOException e) {
            if(!folderStructure.containsKey(ROOT_PATH)) {
                this.folderStructure.put(ROOT_PATH, ROOT);
            }
            this.addFolder("Books", folderStructure.get(ROOT_PATH));
            this.addFolder("Adventure", folderStructure.get(ROOT_PATH + "Books\\"));
            this.addFolder("Fantasy", folderStructure.get(ROOT_PATH + "Books\\"));
            this.addFolder("History", folderStructure.get(ROOT_PATH + "Books\\"));
            this.addFolder("Sci-fi", folderStructure.get(ROOT_PATH + "Books\\"));
            this.save();
        }
        this.allPaths = allPathsToString();

    }

    /**
     * Returns the folder having the given ID.
     *
     * @param path The ID of the requested folder.
     * @return The folder having the given ID.
     */
    public Folder getFolder(String path) {
        return folderStructure.get(path);
    }

    /**
     * Returns all the present paths in the form of a {@code String}.
     *
     * @return A {@code String} containing all paths.
     */
    public String getAllPaths() {
        return allPaths;
    }

    /**
     * Returns the file system data structure as a hash map where the keys are the folders IDs and the values are the
     * actual {@code Folder} objects.
     *
     * @return The file system.
     */
    public HashMap<String, Folder> getFolderStructure() {
        return folderStructure;
    }

    /**
     * Getter for the ID of the ROOT folder.
     *
     * @return The ROOT ID's {@code Long} value.
     */
    public String getRootPath() {
        return ROOT_PATH;
    }

    /**
     * Returns all sub-folders of a desired folder in the form of a {@code String}.
     *
     * @param parentPath The ID associated to the parent whose sub-folders are to be visualized.
     * @return A {@code String} containing all sub-folders.
     */
    public String getSubFolders(String parentPath) {
        StringBuilder folders = new StringBuilder();

        folderStructure.get(parentPath).getChildren()
                .forEach(f -> folders.append(".\t").append(f.getName()).append("\n"));

        return folders.toString().trim();
    }

    /**
     * Adds a folder to the file system.
     *
     * @param name The name of the folder to be added.
     * @param parent Its parent folder.
     */
    public void addFolder(String name, Folder parent) {
        Folder toAdd = new Folder(name, parent);
        folderStructure.put(toAdd.getFolderPath(), toAdd);
        this.save();
    }

    @Override
    public void save() {
        if(this.isSaveEnabled()) {
            try(
                    //to increase serializing speed
                    RandomAccessFile raf = new RandomAccessFile(FILESYSTEM_FILE_PATH, "rw");
                    FileOutputStream fileOut = new FileOutputStream(raf.getFD());
                    ObjectOutputStream out = new ObjectOutputStream(fileOut)
            ) {
                out.writeObject(folderStructure);
            } catch(IOException ioEx) {
                logger.log(Level.SEVERE, Notifications.getMessage("ERR_SAVING_DATABASE ") +
                        this.getClass().getName());
            }
        }
    }

    /**
     * Opens a .ser serializable file and loads its contents into this {@link FileSystem} class.<p>
     * This method loads:
     * <p>- a {@code HashMap} containing all folders;
     * <p>- an {@code integer} to keep track of the ID of the most recently added folder.
     */
    @SuppressWarnings("unchecked")
    private void loadFileSystem() throws IOException {
        try(
            FileInputStream fileIn = new FileInputStream(FILESYSTEM_FILE_PATH);
            ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            this.folderStructure = ((HashMap<String, Folder>) in.readObject());
        }
        catch(ClassNotFoundException cnfEx) {
            logger.log(Level.SEVERE, Notifications.getMessage("ERR_CLASS_NOT_FOUND ") + this.getClass().getName());
        }
    }

    /**
     * Recursive function that prints to string a tree structure starting from a folder.
     *
     * @param root The root folder to start from.
     * @param depth The depth of the current folder.
     * @return A string containing the full tree.
     */
    public String treeToString(Folder root, int depth) {
        StringBuilder folders = new StringBuilder();

        for(int i = 0; i < depth; i++)
            folders.append("\t");
        folders.append(root.getName()).append("\n");

        if(!root.getChildren().isEmpty()) {
            depth++;
            for(Folder f : root.getChildren())
                folders.append(treeToString(f, depth));
        }

        return folders.toString();
    }

    private String allPathsToString() {
        StringBuilder pathBuilder = new StringBuilder();

        folderStructure.values().stream()
                .filter(f -> f.getParent() != f)
                .forEach(f -> pathBuilder.append(f.getFolderPath()).append("\n"));

        return pathBuilder.toString().trim();
    }

    public boolean isSaveEnabled() {
        return saveEnabled;
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

    }

    @Override
    public void removeMedia(Integer mediaId) {

    }

    @Override
    public ArrayList<Field> getTypeValues(String mediaType) throws MediaTypeNotFoundException {
        return null;
    }

    @Override
    public String getMediaList(String category) {
        return null;
    }

    @Override
    public String getFolderTree() {
        return this.treeToString(this.folderStructure.get(ROOT_PATH), 0);
    }

    @Override
    public void isValidPath(String path) throws PathNotFoundException {
        if(!folderStructure.containsKey(path))
            throw new PathNotFoundException();
    }
}
