package contacts.Services;

import contacts.Entity.Contact;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class providing static methods for contact list serialization.
 */
public final class SerializationUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private SerializationUtils() {
        // Prevent instantiation
    }

    /**
     * Serializes the list of contacts and saves it to a file.
     *
     * @param contacts the list of contacts to serialize
     * @param fileName the target file name
     */
    public static void serialize(final List<Contact> contacts,
                                 final String fileName) {
        if (fileName == null) {
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(fileName);
             BufferedOutputStream bos = new BufferedOutputStream(fos);
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            oos.writeObject(contacts);

        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    /**
     * Deserializes the list of contacts from a specified file.
     *
     * @param fileName the source file name
     * @return the deserialized list of contacts, or an empty list if failing
     */
    @SuppressWarnings("unchecked")
    public static List<Contact> deserialize(final String fileName) {
        if (fileName == null) {
            return new ArrayList<>();
        }

        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {

            return (List<Contact>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from file: "
                    + e.getMessage());
            return new ArrayList<>();
        }
    }
}
