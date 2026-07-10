package contacts.Services;

import contacts.Entity.Organization;
import java.util.Scanner;
import static contacts.Main.checkNumber;

/**
 * Service handling operations related to creating organization contacts.
 */
public final class OrganizationService {

    /**
     * Prompts the user to enter data and creates a new Organization instance.
     *
     * @param scanner the scanner instance for reading input
     * @return a newly constructed Organization object
     */
    public Organization newOrganization(final Scanner scanner) {
        System.out.print("Enter the name: ");
        String name = scanner.nextLine();
        System.out.print("Enter the address: ");
        String address = scanner.nextLine().trim();
        System.out.print("Enter the number: ");
        String number = checkNumber(scanner.nextLine());
        return new Organization(
                name,
                number,
                address
        );
    }
}
