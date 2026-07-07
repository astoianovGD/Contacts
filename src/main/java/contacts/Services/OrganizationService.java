package contacts.Services;

import contacts.Entity.Organization;

import java.util.Scanner;
import static contacts.Main.checkNumber;

public class OrganizationService {

    public void editOrganization(Organization organization, Scanner scanner) {
        System.out.print("Select a field (name, number, address) ");
        String field = scanner.nextLine().toLowerCase();
        switch (field) {
            case "name" -> {
                System.out.print("Enter name: ");
                organization.setName(scanner.nextLine());
            }
            case "number" -> {
                System.out.print("Enter number: ");
                organization.setNumber(checkNumber(scanner.nextLine()));
            }
            case "address" -> {
                System.out.print("Enter the address: ");
                organization.setAddress(scanner.nextLine());
            }

            default -> System.out.println("Bad field!");
        }
    }

    public Organization newOrganization(Scanner scanner){
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
