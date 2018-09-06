package main.model.media;

import main.model.media.filesystem.Folder;
import main.utility.data.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static main.utility.InputParserUtility.gregorianDateToString;

/**
 * @author Manuel Gallina
 * @since Version 2
 */
public class Media implements Serializable {
    private int id;
    private String type;
    private Folder category;
    private boolean available;
    private HashMap<Field, String> attributes;

    private GregorianCalendar dateAdded;

    public Media(int id, MediaType mediaType, ArrayList<String> values, Folder category) throws IllegalArgumentException {
        this.id = id;
        this.category = category;
        this.available = true;
        this.attributes = new HashMap<>();

        int size = mediaType.getFields().size();
        if(size != values.size())
            throw new IllegalArgumentException();
        this.type = mediaType.getName();
        for(int i = 0; i < size; i++)
            attributes.put(mediaType.getFields().get(i), values.get(i));
        this.dateAdded = new GregorianCalendar();
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public HashMap<Field, String> getAttributes() {
        return attributes;
    }

    public GregorianCalendar getDateAdded() {
        return dateAdded;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Folder getCategory() {
        return category;
    }

    public void setCategory(Folder category) {
        this.category = category;
    }

    @Override
    public String toString() {
        if(available) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ID: ").append(this.id).append("\t");
            for(Map.Entry<Field, String> fieldStringEntry : attributes.entrySet()) {
                stringBuilder.append(fieldStringEntry.getKey().getName()).append(": ");
                stringBuilder.append(fieldStringEntry.getValue()).append("\t");
            }
            stringBuilder.append("Date Added: ").append(gregorianDateToString(dateAdded)).append("\n");
            return stringBuilder.toString();
        }
        return "";
    }
}
