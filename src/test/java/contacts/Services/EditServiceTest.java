package contacts.Services;

import contacts.Entity.Contact;
import contacts.Entity.Organization;
import contacts.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class EditServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final java.io.InputStream standardIn = System.in;

    private EditService editService;
    private List<Contact> contacts;
    private Contact testContact;
    private static final String TEST_FILE = "test_contacts.db";

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        editService = new EditService();
        contacts = new ArrayList<>();
        testContact = new Organization("Initial Name", "123456", "Initial Address");
        contacts.add(testContact);
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);

        // Clean up created serialization file if any matches Main.getFileName()
        try {
            new File(TEST_FILE).delete();
            if (Main.getFileName() != null) {
                new File(Main.getFileName()).delete();
            }
        } catch (Exception ignored) {
        }
    }

    private Scanner createMockScanner(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        return new Scanner(inputStream);
    }

    @Test
    @DisplayName("Should return menu immediately when action is menu")
    void testActionMenu() {
        Scanner scanner = createMockScanner("menu\n");

        String result = editService.editActions(contacts, testContact, scanner);

        assertEquals("menu", result);
    }

    @Test
    @DisplayName("Should remove contact and return menu when action is delete")
    void testActionDelete() {
        Scanner scanner = createMockScanner("delete\n");
        int initialSize = contacts.size();

        String result = editService.editActions(contacts, testContact, scanner);

        assertAll("Verify delete workflow",
                () -> assertEquals("menu", result),
                () -> assertEquals(initialSize - 1, contacts.size()),
                () -> assertFalse(contacts.contains(testContact))
        );
    }

    @Test
    @DisplayName("Should successfully edit field, serialize, and then exit recursion via menu")
    void testActionEditField() {
        //  edit -> select field 'name' -> enter value 'New Dynamic Name' -> exit loop via 'menu'
        Scanner scanner = createMockScanner("edit\nname\nNew Dynamic Name\nmenu\n");

        String result = editService.editActions(contacts, testContact, scanner);

        assertAll("Verify edit workflow details",
                () -> assertEquals("menu", result),
                () -> assertEquals("New Dynamic Name", testContact.getName()),
                () -> assertTrue(outputStreamCaptor.toString().contains("Saved"))
        );
    }

    @Test
    @DisplayName("Should handle wrong action, print warning, and recover recursion via menu")
    void testWrongActionHandling() {
        //  invalid_command -> menu (to break recursion safely)
        Scanner scanner = createMockScanner("invalid_command\nmenu\n");

        String result = editService.editActions(contacts, testContact, scanner);

        assertAll("Verify wrong option recovery",
                () -> assertEquals("menu", result),
                () -> assertTrue(outputStreamCaptor.toString().contains("Wrong action!"))
        );
    }
}