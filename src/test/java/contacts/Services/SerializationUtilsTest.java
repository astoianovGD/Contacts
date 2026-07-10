package contacts.Services;

import contacts.Entity.Contact;
import contacts.Entity.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SerializationUtilsTest {

    private static final String VALID_TEST_FILE = "test_contacts_valid.db";
    private static final String CORRUPTED_TEST_FILE = "test_contacts_corrupted.db";
    private List<Contact> sampleContacts;

    @BeforeEach
    void setUp() {
        sampleContacts = new ArrayList<>();
        sampleContacts.add(new Organization("Grid Dynamics", "123456", "Kharkiv"));
    }

    @AfterEach
    void tearDown() {
        // Cleaning up temporary testing files from the disk
        try {
            Files.deleteIfExists(Paths.get(VALID_TEST_FILE));
            Files.deleteIfExists(Paths.get(CORRUPTED_TEST_FILE));
        } catch (IOException ignored) {
        }
    }

    @Test
    @DisplayName("Should successfully serialize and deserialize a valid contact list")
    void testSuccessfulSerializationWorkflow() {
        SerializationUtils.serialize(sampleContacts, VALID_TEST_FILE);

        File file = new File(VALID_TEST_FILE);
        assertTrue(file.exists(), "The serialized data file must be physically created");

        List<Contact> deserializedList = SerializationUtils.deserialize(VALID_TEST_FILE);

        assertAll("Verify deserialized list contents",
                () -> assertNotNull(deserializedList),
                () -> assertEquals(1, deserializedList.size()),
                () -> assertEquals("Grid Dynamics", deserializedList.get(0).getName())
        );
    }

    @Test
    @DisplayName("Should handle null file names safely in both static methods")
    void testNullFileNameHandling() {
        // Serialize should return gracefully without crashing
        assertDoesNotThrow(() -> SerializationUtils.serialize(sampleContacts, null));

        // Deserialize should return an empty list when filename is null
        List<Contact> result = SerializationUtils.deserialize(null);
        assertAll("Verify null input defense structure",
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    @DisplayName("Should return empty array list when trying to load a non-existent file")
    void testNonExistentFileHandling() {
        List<Contact> result = SerializationUtils.deserialize("this_file_does_not_exist_at_all.db");
        assertAll("Verify missing file fallback",
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    @DisplayName("Should catch IOException and return empty list when deserializing corrupted files")
    void testCorruptedFileDeserializationException() throws IOException {
        // Create a fake file with plain garbage data instead of real Java serialized objects
        Path corruptedPath = Paths.get(CORRUPTED_TEST_FILE);
        Files.writeString(corruptedPath, "Plain text dynamic garbage that breaks ObjectInputStream stream headers");

        List<Contact> result = SerializationUtils.deserialize(CORRUPTED_TEST_FILE);

        assertAll("Verify catch block execution path",
                () -> assertNotNull(result),
                () -> assertTrue(result.isEmpty())
        );
    }

    @Test
    @DisplayName("Technical test to cover the private utility constructor for full coverage")
    void testPrivateConstructor() throws Exception {
        java.lang.reflect.Constructor<SerializationUtils> constructor =
                SerializationUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        SerializationUtils instance = constructor.newInstance();
        assertNotNull(instance);
    }
}