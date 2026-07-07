package contacts.Services;

import contacts.Entity.Person;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static contacts.Main.checkNumber;

public class PersonService {

    public void editPerson(Person person, Scanner scanner) {
        System.out.print("Select a field (name, surname, number, birth, gender) ");
        String field = scanner.nextLine().toLowerCase();
        switch (field) {
            case "name" -> {
                System.out.print("Enter name: ");
                person.setName(scanner.nextLine());
            }
            case "surname" -> {
                System.out.print("Enter surname: ");
                person.setSurname(scanner.nextLine());
            }
            case "number" -> {
                System.out.print("Enter number: ");
                person.setNumber(checkNumber(scanner.nextLine()));
            }
            case "birth" -> {
                System.out.print("Enter the birth date: ");
                String inputDate = scanner.nextLine();
                person.setBirth(checkBirth(inputDate));
            }
            case "gender" -> {
                System.out.print("Enter gender (M,F): ");
                String gender = scanner.nextLine().toUpperCase().trim();
                person.setGender(checkGender(gender));
            }
            default -> System.out.println("Bad field!");
        }
    }

    public Person newPerson(Scanner scanner){
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

    public String checkGender(String gender){
        if (!gender.equals("M") && !gender.equals("F")) {
            System.out.println("Bad gender!");
            return "[no data]";
        }
        return gender;

    }

    public String checkBirth(String birth) {
        String birthDate;
        try {
            LocalDate.parse(birth);
            birthDate = birth;
        } catch (DateTimeParseException e) {
            System.out.println("Bad birth date!");
            birthDate = "[no data]";
        }
        return birthDate;
    }

}
