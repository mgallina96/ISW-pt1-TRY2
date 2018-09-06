package main.model.media;

import main.utility.data.DataType;
import main.utility.data.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Manuel Gallina
 * @since Version 2
 */
public class MediaType implements Serializable {
    private String name;
    private ArrayList<Field> fields;

    public MediaType(String name, ArrayList<Field> fields) {
        this.name = name;
        this.fields = fields;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }
}
