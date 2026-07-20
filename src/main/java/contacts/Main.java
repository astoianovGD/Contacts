package contacts;

import contacts.Entity.Contact;
import contacts.Enums.Action;
import contacts.Exceptions.BadIndexException;
import contacts.Services.ListService;
import contacts.Services.OrganizationService;
import contacts.Services.PersonService;
import contacts.Services.SearchService;
import contacts.Services.SerializationUtils;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class acting as the entry point for the contact system.
 */
public final class Main {
    /** The active collection of contacts. */
    private static List<Contact> contacts = new ArrayList<>();
    /** The storage file name for data serialization. */
    private static String fileName = null;

    /**
     * Private constructor to hide the implicit public one on utility classes.
     */
    private Main() {
        // Prevent instantiation
    }

    /**
     * The main execution loop of the phone book application.
     *
     * @param args command-line arguments, expects file name as first parameter
     */
    public static void main(final String[] args) {
        Scanner scanner = new Scanner(new InputStreamReader(
                System.in, StandardCharsets.UTF_8)
        );
        if (args.length > 0) {
            fileName = args[0];
            System.out.println("open " + fileName);
        }

        contacts = SerializationUtils.deserialize(fileName);

        while (true) {
            System.out.print("[menu] Enter action "
                    + "(add, list, search, count, exit): ");
            String input = scanner.nextLine().trim().toUpperCase();
            Action action;
            try {
                action = Action.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Wrong action!");
                continue;
            }

            if (action == Action.EXIT) {
                return;
            }

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

    /**
     * Routes the selected application action to its specific handler service.
     *
     * @param action  the chosen application Action enum
     * @param scanner the scanner instance for user input parsing
     * @throws BadIndexException if handling requests an index out of bounds
     */
    public static void enterAction(final Action action, final Scanner scanner)
            throws BadIndexException {
        switch (action) {
            case ADD -> {
                System.out.print("Enter the type (person, organization): ");
                String type = scanner.nextLine().toLowerCase();

                if (!type.equals("person") && !type.equals("organization")) {
                    System.out.println("Bad type!");
                    return;
                }

                OrganizationService organizationService =
                        new OrganizationService();
                PersonService personService = new PersonService();

                contacts.add(type.equals("person")
                        ? personService.newPerson(scanner)
                        : organizationService.newOrganization(scanner));
                System.out.println("The record added.");
                SerializationUtils.serialize(contacts, fileName);
            }
            case SEARCH -> {
                SearchService searchService = new SearchService();
                searchService.searchAction(contacts, scanner);
            }
            case COUNT -> System.out.println("The Phone Book has "
                    + contacts.size() + " records.");
            case LIST -> {
                ListService listService = new ListService();
                listService.listAction(contacts, scanner);
            }
            default -> System.out.println("Wrong action!");
        }
    }

    /**
     * Validates if a phone number string matches standard format requirements.
     *
     * @param number the raw number string input
     * @return the valid number or a default error token placeholder
     */
    public static String checkNumber(final String number) {
        String regexp = "^\\+?(?:[a-zA-Z0-9]+(?:[ -][a-zA-Z0-9]{2,})*|\\("
                + "[a-zA-Z0-9]+\\)(?:[ -][a-zA-Z0-9]{2,})*|[a-zA-Z0-9]+[ -]"
                + "\\([a-zA-Z0-9]{2,}\\)(?:[ -][a-zA-Z0-9]{2,})*)$";
        if (number.matches(regexp)) {
            return number;
        } else {
            System.out.println("Wrong number format!");
            return "[no number]";
        }
    }

    /**
     * Safely fetches a specific contact matching user-provided numeric inputs.
     *
     * @param contactList the targeted database lookup collection
     * @param userIndex   the index input from standard output (1-based index)
     * @return the resolved contact context entity
     * @throws BadIndexException
     * if the requested index sits outside valid ranges
     */
    public static Contact getContactSafe(final List<Contact> contactList,
                                         final int userIndex)
            throws BadIndexException {
        int realIndex = userIndex - 1;
        if (realIndex < 0 || realIndex >= contactList.size()) {
            throw new BadIndexException();
        }
        return contactList.get(realIndex);
    }

    /**
     * Prints all contacts onto terminal standard outputs.
     *
     * @param contactList the target records loop array list
     */
    public static void printAllContacts(final List<Contact> contactList) {
        for (int i = 0; i < contactList.size(); i++) {
            System.out.println((i + 1) + ". " + contactList.get(i));
        }
    }

    /**
     * Gets the serialization file name path configuration.
     *
     * @return the file location name string configured
     */
    public static String getFileName() {
        return fileName;
    }

    /**
     * Configures the active serialization output location manually.
     *
     * @param name target data save address string location value
     */
    public static void setFileName(final String name) {
        fileName = name;
    }
}
