package contacts.Services;

import contacts.Entity.Contact;
import contacts.Main;

import java.util.List;
import java.util.Scanner;

public class EditingService {
    public String editActions(List<Contact> contacts, Contact contact, Scanner scanner) {
        System.out.print("[record] Enter action (edit, delete, menu): ");
        String action = scanner.nextLine();
        switch (action.toLowerCase()) {
            case "edit" -> {
                List<String> fields = contact.getEditableFields();
                System.out.print("Select a field (" + String.join(", ", fields) + "): ");
                String field = scanner.nextLine();
                System.out.printf("Enter %s: ", field);
                contact.setFieldValue(field.toLowerCase(), scanner.nextLine());
                SerializationUtils.serialize(contacts, Main.fileName);
                System.out.println("Saved");
                System.out.println(contact.getDetailedInfo());
                System.out.println();

                return editActions(contacts, contact, scanner);
            }
            case "delete" -> {
                contacts.remove(contact);
                SerializationUtils.serialize(contacts, Main.fileName);
                return "menu";
            }
            case "menu" -> {
                return "menu";
            }
            default -> {
               System.out.println("Wrong action!");
                return editActions(contacts, contact, scanner);
            }
        }
    }
}
