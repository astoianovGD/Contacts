package contacts;

import contacts.Entity.Contact;
import contacts.Entity.Organization;
import contacts.Entity.Person;
import contacts.Enums.Action;
import contacts.Exceptions.BadIndexException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final java.io.InputStream standardIn = System.in;
    private static final String TEST_FILE = "main_test_contacts.db";

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        Main.setFileName(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setIn(standardIn);

        // Clean up any serialization side-effects
        try {
            new File(TEST_FILE).delete();
        } catch (Exception ignored) {
        }
    }

    private Scanner createMockScanner(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        return new Scanner(inputStream);
    }

    @Test
    @DisplayName("Should process command line arguments and launch exit loop successfully")
    void testMainWithArgsAndExit() {
        Scanner scanner = createMockScanner("exit\n");

        // Simulating invocation with args array
        Main.main(new String[]{TEST_FILE});

        assertAll("Verify startup log and configuration",
                () -> assertEquals(TEST_FILE, Main.getFileName()),
                () -> assertTrue(outputStreamCaptor.toString().contains("open " + TEST_FILE))
        );
    }

    @Test
    @DisplayName("Should print warning and continue loop when action is invalid")
    void testMainInvalidActionLoop() {
        // invalid_command -> exit
        Scanner scanner = createMockScanner("invalid_command\nexit\n");

        Main.main(new String[]{});

        assertTrue(outputStreamCaptor.toString().contains("Wrong action!"));
    }

    @Test
    @DisplayName("Should successfully handle count action from menu workflow")
    void testEnterActionCount() throws BadIndexException {
        Scanner scanner = createMockScanner("");

        Main.enterAction(Action.COUNT, scanner);

        assertTrue(outputStreamCaptor.toString().contains("The Phone Book has"));
    }

    @Test
    @DisplayName("Should handle add action workflow with bad contact type specification")
    void testEnterActionAddBadType() throws BadIndexException {
        Scanner scanner = createMockScanner("alien\n");

        Main.enterAction(Action.ADD, scanner);

        assertTrue(outputStreamCaptor.toString().contains("Bad type!"));
    }

    @Test
    @DisplayName("Should successfully add a new organization contact through route")
    void testEnterActionAddOrganizationSuccess() throws BadIndexException {
        // type -> name -> address -> valid number
        Scanner scanner = createMockScanner("organization\nTest Corp\nKyiv\n+380441234567\n");

        Main.enterAction(Action.ADD, scanner);

        assertAll("Verify successful inclusion response",
                () -> assertTrue(outputStreamCaptor.toString().contains("The record added."))
        );
    }

    @Test
    @DisplayName("Should successfully add a new person contact through route")
    void testEnterActionAddPersonSuccess() throws BadIndexException {
        // ype -> name -> surname -> birth date -> gender -> valid number
        Scanner scanner = createMockScanner("person\nJohn\nDoe\n1990-01-01\nM\n+380501234567\n");

        Main.enterAction(Action.ADD, scanner);

        assertAll("Verify human contact routing details",
                () -> assertTrue(outputStreamCaptor.toString().contains("The record added."))
        );
    }

    @Test
    @DisplayName("Should route to search action handler workflow context")
    void testEnterActionSearchRoute() throws BadIndexException {
        Scanner scanner = createMockScanner("NonExistentQuery\n");

        Main.enterAction(Action.SEARCH, scanner);

        assertTrue(outputStreamCaptor.toString().contains("Enter search query:"));
    }

    @Test
    @DisplayName("Should route to list action handler workflow context")
    void testEnterActionListRoute() throws BadIndexException {
        Scanner scanner = createMockScanner("back\n");

        Main.enterAction(Action.LIST, scanner);

        // If empty list, prints message. If populated, proceeds. Either way, listAction runs.
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("The Phone Book is empty!") || output.contains("[list]"));
    }

    @Test
    @DisplayName("Should fall back to default behavior for unrouted action triggers")
    void testEnterActionDefaultFallback() throws BadIndexException {
        // Using EXIT as an unrouted trigger inside switch block
        Scanner scanner = createMockScanner("");

        Main.enterAction(Action.EXIT, scanner);

        assertTrue(outputStreamCaptor.toString().contains("Wrong action!"));
    }

    @Test
    @DisplayName("Test number validation pattern for multiple formatting permutations")
    void testCheckNumberRegexScenarios() {
        assertAll("Verify regex boundaries",
                // Valid structures
                () -> assertEquals("+123 (45) 678-90", Main.checkNumber("+123 (45) 678-90")),
                () -> assertEquals("0501234567", Main.checkNumber("0501234567")),
                // Invalid formats
                () -> assertEquals("[no number]", Main.checkNumber("++123")),
                () -> assertEquals("[no number]", Main.checkNumber("123 (45) (67)"))
        );
    }

    @Test
    @DisplayName("Should return requested entity or throw BadIndexException when querying index safely")
    void testGetContactSafeBounds() {
        List<Contact> list = new ArrayList<>();
        list.add(new Organization("Org", "123", "Addr"));

        assertAll("Verify list indexing security",
                // Happy Path (1-based index converted to 0)
                () -> assertNotNull(Main.getContactSafe(list, 1)),
                // Lower bounds violation
                () -> assertThrows(BadIndexException.class, () -> Main.getContactSafe(list, 0)),
                // Upper bounds violation
                () -> assertThrows(BadIndexException.class, () -> Main.getContactSafe(list, 5))
        );
    }

    @Test
    @DisplayName("Should loop and display formatted representation text of contacts collection items")
    void testPrintAllContactsLoop() {
        List<Contact> list = new ArrayList<>();
        list.add(new Person("Alexey", "Stoyanow", "+380501234567", "2000-01-01", "M"));

        Main.printAllContacts(list);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("1. Alexey Stoyanow"), "Console output should contain formatted index and person name");
    }

    @Test
    @DisplayName("Technical test to cover utility private constructor constraints")
    void testPrivateConstructor() throws Exception {
        java.lang.reflect.Constructor<Main> constructor = Main.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Main instance = constructor.newInstance();
        assertNotNull(instance);
    }
}