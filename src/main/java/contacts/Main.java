package contacts;

import contacts.Entity.Contact;
import contacts.Enums.Action;
import contacts.Exceptions.BadIndexException;
import contacts.Services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static List<Contact> contacts = new ArrayList<>();
    public static String fileName = null;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (args.length > 0) {
            fileName = args[0];
            System.out.println("open " + fileName);
        }

        contacts = SerializationUtils.deserialize(fileName);

        while (true) {
            System.out.print("[menu] Enter action (add, list, search, count, exit): ");
            String input = scanner.nextLine().trim().toUpperCase();
            Action action;
            try {
                action = Action.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong action!");
                continue;
            }

            if (action == Action.EXIT) return;

            try {
                enterAction(action, scanner);
            } catch (BadIndexException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid index format!");
            }
            System.out.println();
        }

    }


    public static void enterAction(Action action, Scanner scanner) throws BadIndexException {
        switch (action) {
            case ADD -> {
                System.out.print("Enter the type (person, organization): ");
                String type = scanner.nextLine().toLowerCase();

                if (!type.equals("person") && !type.equals("organization")) {
                    System.out.println("Bad type!");
                    return;
                }

                OrganizationService organizationService = new OrganizationService();
                PersonService personService = new PersonService();

                contacts.add(type.equals("person")  ? personService.newPerson(scanner) : organizationService.newOrganization(scanner));
                System.out.println("The record added.");
                SerializationUtils.serialize(contacts, fileName);

            }
            case SEARCH -> {
                SearchService searchService = new SearchService();
                searchService.searchAction(contacts, scanner);

            }
            case COUNT -> System.out.println("The Phone Book has " + contacts.size() + " records.");

            case LIST -> {
                ListService listService = new ListService();
                listService.listAction(contacts, scanner);

            }
            default -> System.out.println("Wrong action!");
        }
    }


    public static String checkNumber(String number) {
        String regexp = "^\\+?(?:[a-zA-Z0-9]+(?:[ -][a-zA-Z0-9]{2,})*|\\([a-zA-Z0-9]+\\)(?:[ -][a-zA-Z0-9]{2,})*|[a-zA-Z0-9]+[ -]\\([a-zA-Z0-9]{2,}\\)(?:[ -][a-zA-Z0-9]{2,})*)$";
        if (number.matches(regexp)) return number;
        else {
            System.out.println("Wrong number format!");
            return "[no number]";
        }
    }

    public static Contact getContactSafe(List<Contact> contacts, int userIndex) throws BadIndexException {
        int realIndex = userIndex - 1;
        if (realIndex < 0 || realIndex >= contacts.size()) {
            throw new BadIndexException();
        }
        return contacts.get(realIndex);
    }

    public static void printAllContacts(List<Contact> contactList) {
        for (int i = 0; i < contactList.size(); i++) {
            System.out.println((i + 1) + ". " + contactList.get(i));
        }
    }


}
