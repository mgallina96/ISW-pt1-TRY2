package main.utility.data;

import java.io.Serializable;

/**
 * @author Manuel Gallina
 */
public class Field implements Serializable {
    private String name;
    private DataType type;

    public Field(String name, DataType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }
}
