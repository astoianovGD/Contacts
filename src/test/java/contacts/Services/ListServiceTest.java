package contacts.Services;

import contacts.Entity.Contact;
import contacts.Entity.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ListServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final java.io.InputStream standardIn = System.in;

    private ListService listService;
    private List<Contact> contacts;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        listService = new ListService();
        contacts = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);
    }

    private Scanner createMockScanner(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        return new Scanner(inputStream);
    }

    @Test
    @DisplayName("Should print message and return immediately when contact list is empty")
    void testListActionWithEmptyList() {
        Scanner scanner = createMockScanner("back\n");

        listService.listAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("The Phone Book is empty!"));
    }

    @Test
    @DisplayName("Should return immediately when action is back")
    void testListActionWithBackCommand() {
        // Adding one contact so the list is not empty
        contacts.add(new Organization("Test Org", "123", "Test Addr"));
        Scanner scanner = createMockScanner("back\n");

        listService.listAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertFalse(output.contains("Wrong action!"));
    }

    @Test
    @DisplayName("Should print warning when action is invalid and then exit via back")
    void testListActionWithInvalidAction() {
        contacts.add(new Organization("Test Org", "123", "Test Addr"));
        // Sequence: invalid_command -> back (to break the while loop)
        Scanner scanner = createMockScanner("invalid_command\nback\n");

        listService.listAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Wrong action!"));
    }

    @Test
    @DisplayName("Should handle wrong index gracefully when BadIndexException is thrown")
    void testListActionWithWrongIndex() {
        contacts.add(new Organization("Test Org", "123", "Test Addr"));
        // Sequence: 99 (invalid index) -> back (to break the while loop)
        Scanner scanner = createMockScanner("99\nback\n");

        listService.listAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Wrong index! Try again."));
    }

    @Test
    @DisplayName("Should view contact details and delegate to editActions when a valid index is provided")
    void testListActionWithValidIndex() {
        Contact contact = new Organization("Grid Dynamics", "+380501234567", "Kharkiv");
        contacts.add(contact);

        // Sequence: 1 (valid index, assuming 1-based index processing in Main.getContactSafe)
        // -> menu (to exit editActions immediately with "menu" response, which breaks the listAction loop)
        Scanner scanner = createMockScanner("1\nmenu\n");

        listService.listAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertAll("Verify execution path via valid index selection",
                () -> assertTrue(output.contains("Organization name: Grid Dynamics")),
                () -> assertTrue(output.contains("Number: +380501234567"))
        );
    }
}