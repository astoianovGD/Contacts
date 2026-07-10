package contacts.Services;

import contacts.Entity.Person;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Scanner;
import static contacts.Main.checkNumber;

/**
 * Service handling operations related to creating and
 * validating person contacts.
 */
public final class PersonService {

    /**
     * Prompts the user to enter data and creates a new Person instance.
     *
     * @param scanner the scanner instance for reading input
     * @return a newly constructed Person object
     */
    public Person newPerson(final Scanner scanner) {
        System.out.print("Enter the name: ");
        String name = scanner.nextLine();

        System.out.print("Enter the surname: ");
        String surname = scanner.nextLine();

        System.out.print("Enter the birth date: ");
        String birth = checkBirth(scanner.nextLine());

        System.out.print("Enter the gender (M, F): ");
        String gender = checkGender(scanner.nextLine().toUpperCase().trim());

        System.out.print("Enter the number: ");
        String number = checkNumber(scanner.nextLine());

        return new Person(name, surname, number, birth, gender);
    }

    /**
     * Validates and returns the gender string.
     *
     * @param gender the raw gender input string
     * @return the validated gender string, or a placeholder if invalid
     */
    public String checkGender(final String gender) {
        if (!gender.equals("M") && !gender.equals("F")) {
            System.out.println("Bad gender!");
            return "[no data]";
        }
        return gender;
    }

    /**
     * Validates and returns the birthdate string.
     *
     * @param birth the raw birthdate input string
     * @return the validated birthdate string, or a placeholder if invalid
     */
    public String checkBirth(final String birth) {
        if (isValidDate(birth)) {
            return birth;
        } else {
            System.out.println("Bad birth date!");
            return "[no data]";
        }
    }

    /**
     * Helper method to verify if a string is a valid ISO date format.
     *
     * @param dateStr the string to test
     * @return true if valid, false otherwise
     */
    private boolean isValidDate(final String dateStr) {
        try {
            Objects.requireNonNull(LocalDate.parse(dateStr));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
