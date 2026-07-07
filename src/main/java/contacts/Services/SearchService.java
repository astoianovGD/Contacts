package contacts.Services;

import contacts.Entity.Contact;
import contacts.Exceptions.BadIndexException;
import contacts.Main;

import java.util.List;
import java.util.Scanner;

//1) search -> print all founded
//2) [search] Enter action ([number], back, again):
//3) if number [record] Enter action (edit, delete, menu):
public class SearchService {

    private final EditingService editingService = new EditingService();

    public void searchAction(List<Contact> list, Scanner scanner) {
        System.out.print("Enter search query: ");
        String keyword = scanner.nextLine();
        List<Contact> foundedContacts = list.stream().filter(l -> l.isContainsASearchWord(keyword)).toList();
        if (foundedContacts.isEmpty()) {
            System.out.println("Nobody has been found!");
            return;
        } else {
            Main.printAllContacts(foundedContacts);
            while (true) {
                System.out.print("[search] Enter action ([number], back, again): ");
                String action = scanner.nextLine();
                switch (action.toLowerCase()) {
                    case "back" -> {
                        return;
                    }
                    case "again" -> {
                        searchAction(list, scanner);
                        return;
                    }
                    case String s when s.matches("\\d+") -> {
                        int index = Integer.parseInt(s);
                        try {
                            Contact contact = Main.getContactSafe(foundedContacts, index);
                            System.out.println(contact.getDetailedInfo());
                            System.out.println();
                            if (editingService.editActions(list, contact, scanner).equals("menu")) return;
                        } catch (BadIndexException e) {
                            System.out.println("Wrong index! Try again.");
                        }
                    }
                    default -> System.out.println("Wrong action!");
                }
            }
        }
    }
}

