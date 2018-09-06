package main.view.listeners;

import main.utility.data.Field;
import main.utility.exceptions.MediaNotFoundException;
import main.utility.exceptions.MediaTypeNotFoundException;
import main.utility.exceptions.PathNotFoundException;

import java.nio.file.InvalidPathException;
import java.util.ArrayList;

/**
 * @author Manuel Gallina
 * @since Version 1
 */
public interface OperatorScreenListener {
    String getCurrentUserName();
    String getUserList();

    void addMedia(String mediaType, ArrayList<String> values, String category);
    void removeMedia(Integer mediaId) throws MediaNotFoundException;
    ArrayList<Field> getTypeValues(String mediaType) throws MediaTypeNotFoundException;
    String getMediaList(String category) throws PathNotFoundException;

    String getFolderTree();
    void isValidPath(String path) throws PathNotFoundException;
}
