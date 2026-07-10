package contacts.Services;

import contacts.Entity.Contact;
import contacts.Exceptions.BadIndexException;
import contacts.Main;
import java.util.List;
import java.util.Scanner;

/**
 * Service handling operations related to listing contacts.
 */
public final class ListService {

    /** Service used to handle record editing workflows. */
    private final EditService editService = new EditService();

    /**
     * Lists all contacts and handles interactive choices by record number.
     *
     * @param list    the list of contacts to display and manage
     * @param scanner the scanner instance for reading input
     */
    public void listAction(final List<Contact> list, final Scanner scanner) {
        if (list.isEmpty()) {
            System.out.println("The Phone Book is empty!");
            return;
        }

        Main.printAllContacts(list);
        System.out.println();

        while (true) {
            System.out.print("[list] Enter action ([number], back): ");
            String action = scanner.nextLine().trim();

            if ("back".equals(action)) {
                return;
            }

            if (action.matches("\\d+")) {
                int index = Integer.parseInt(action);
                try {
                    Contact contact = Main.getContactSafe(list, index);

                    System.out.println(contact.getDetailedInfo());
                    System.out.println();

                    String nextStep = editService.editActions(list,
                            contact, scanner);
                    if ("menu".equals(nextStep)) {
                        return;
                    }
                } catch (BadIndexException e) {
                    System.out.println("Wrong index! Try again.");
                }
            } else {
                System.out.println("Wrong action!");
            }
        }
    }
}
