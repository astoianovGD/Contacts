package contacts.Services;

import contacts.Entity.Contact;
import contacts.Exceptions.BadIndexException;
import contacts.Main;
import java.util.List;
import java.util.Scanner;

/**
 * Service handling operations related to searching contacts.
 */
public final class SearchService {

    /** Service used to handle record editing workflows. */
    private final EditService editService = new EditService();

    /**
     * Searches for contacts based on a user query and handles sub-actions.
     *
     * @param list    the global list of all contacts
     * @param scanner the scanner instance for reading input
     */
    public void searchAction(final List<Contact> list, final Scanner scanner) {
        System.out.print("Enter search query: ");
        String keyword = scanner.nextLine();
        List<Contact> foundedContacts = list.stream()
                .filter(l -> l.isContainsASearchWord(keyword))
                .toList();

        if (foundedContacts.isEmpty()) {
            System.out.println("Nobody has been found!");
        } else {
            Main.printAllContacts(foundedContacts);
            while (true) {
                System.out.print(
                        "[search] Enter action ([number], back, again): ");
                String action = scanner.nextLine();
                switch (action.toLowerCase()) {
                    case "back" -> {
                        return;
                    }
                    case "again" -> {
                        searchAction(list, scanner);
                        return;
                    }
                    default -> {
                        if (action.matches("\\d+")) {
                            int index = Integer.parseInt(action);

                            try {
                                Contact contact = Main.getContactSafe(
                                        foundedContacts, index);
                                System.out.println(contact.getDetailedInfo());
                                System.out.println();

                                String nextAction = editService.editActions(
                                        list, contact, scanner);
                                if ("menu".equals(nextAction)) {
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
        }
    }
}
