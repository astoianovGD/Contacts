package contacts.Services;

import contacts.Entity.Contact;
import contacts.Main;
import java.util.List;
import java.util.Scanner;

/**
 * Service handling operations related to editing records.
 */
public final class EditService {

    /**
     * Handles the record interaction workflow (edit, delete, menu).
     *
     * @param contacts the list of all available contacts
     * @param contact  the specific contact to manipulate
     * @param scanner  the scanner instance for reading input
     * @return the next action string for navigation
     */
    public String editActions(final List<Contact> contacts,
                              final Contact contact,
                              final Scanner scanner) {
        System.out.print("[record] Enter action (edit, delete, menu): ");
        String action = scanner.nextLine();
        switch (action.toLowerCase()) {
            case "edit" -> {
                List<String> fields = contact.getEditableFields();
                System.out.print("Select a field ("
                        + String.join(", ", fields) + "): ");
                String field = scanner.nextLine();
                System.out.printf("Enter %s: ", field);
                contact.setFieldValue(field.toLowerCase(), scanner.nextLine());
                SerializationUtils.serialize(contacts, Main.getFileName());
                System.out.println("Saved");
                System.out.println(contact.getDetailedInfo());
                System.out.println();

                return editActions(contacts, contact, scanner);
            }
            case "delete" -> {
                contacts.remove(contact);
                SerializationUtils.serialize(contacts, Main.getFileName());
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
