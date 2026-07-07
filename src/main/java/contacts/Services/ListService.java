package contacts.Services;

import contacts.Entity.Contact;
import contacts.Exceptions.BadIndexException;
import contacts.Main;

import java.util.List;
import java.util.Scanner;

public class ListService {

    private final EditingService editingService = new EditingService();

    public void listAction(List<Contact> list, Scanner scanner) {
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
                    if ("menu".equals(editingService.editActions(list, contact, scanner))) {
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