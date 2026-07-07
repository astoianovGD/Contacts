package contacts.Services;

import contacts.Entity.Contact;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationUtils {

    public static void serialize(List<Contact> contacts, String fileName) {
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

    @SuppressWarnings("unchecked")
    public static List<Contact> deserialize(String fileName) {
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
            System.out.println("Error loading data from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
