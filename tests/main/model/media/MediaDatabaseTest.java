package main.model.media;

import main.model.media.filesystem.FileSystem;
import main.utility.data.DataType;
import main.utility.data.Field;
import main.utility.exceptions.MediaNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Manuel Gallina
 */
class MediaDatabaseTest {
    private FileSystem testFileSystem;

    private MediaDatabase testDatabase1;
    private MediaDatabase testDatabase2;

    @BeforeEach
    void setUp() {
        testFileSystem = new FileSystem(false);
        testFileSystem.initDatabase();
        testDatabase1 = new MediaDatabase(false, testFileSystem);
        testDatabase1.initDatabase();
        testDatabase2 = new MediaDatabase(false, testFileSystem);
        testDatabase2.initDatabase();
    }

    @Test
    void addMedia() {
        ArrayList<Field> testTypeFields = new ArrayList<>();
        ArrayList<String> testValues1 = new ArrayList<>();

        testFileSystem.addFolder("testCategory", testFileSystem.getFolder(testFileSystem.getRootPath()));
        testTypeFields.add(new Field("title", DataType.STRING));
        testValues1.add("title1");
        testDatabase1.addMediaType("testType", testTypeFields);

        int reference = testDatabase1.getMediaNumber();

        testDatabase1.addMedia("testType", testValues1, "testCategory");

        assertEquals(reference + 1, testDatabase1.getMediaNumber());
    }

    @Test
    void removeMedia() {
        ArrayList<Field> testTypeFields = new ArrayList<>();
        ArrayList<String> testValues1 = new ArrayList<>();
        Field testField = new Field("title", DataType.STRING);
        MediaType testType = new MediaType("testType", testTypeFields);

        testFileSystem.addFolder("testCategory", testFileSystem.getFolder(testFileSystem.getRootPath()));
        testTypeFields.add(testField);
        testValues1.add("title1");
        testDatabase2.addMediaType(testType.getName(), testType.getFields());

        int reference = testDatabase1.getMediaNumber();
        int toRemove = 0;

        testDatabase2.addMedia("testType", testValues1, "testCategory");
        for(Map.Entry<Integer, Media> mediaEntry : testDatabase2.getMediaData().entrySet())
            if(mediaEntry.getValue().getType().equals(testType.getName()))
                toRemove = mediaEntry.getKey();

        int finalToRemove = toRemove;
        Executable test = () -> testDatabase2.removeMedia(finalToRemove);

        assertDoesNotThrow(test);
        assertEquals(reference, testDatabase2.getMediaNumber());
        assertThrows(MediaNotFoundException.class, test);
    }
}