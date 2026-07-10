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

class SearchServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final java.io.InputStream standardIn = System.in;

    private SearchService searchService;
    private List<Contact> contacts;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        searchService = new SearchService();
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
    @DisplayName("Should print message when no contacts match the search query")
    void testSearchNobodyFound() {
        Scanner scanner = createMockScanner("NonExistentName\n");

        searchService.searchAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Nobody has been found!"));
    }

    @Test
    @DisplayName("Should return immediately when sub-action is back")
    void testSearchActionBack() {
        contacts.add(new Organization("Grid Dynamics", "123", "Kharkiv"));
        //  search query 'Grid' -> sub-action 'back'
        Scanner scanner = createMockScanner("Grid\nback\n");

        searchService.searchAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertFalse(output.contains("Wrong action!"));
    }

    @Test
    @DisplayName("Should support recursive search when sub-action is again")
    void testSearchActionAgain() {
        contacts.add(new Organization("Grid Dynamics", "123", "Kharkiv"));
        // search 'Grid' -> action 'again' -> second search 'NonExistent' to break loop
        Scanner scanner = createMockScanner("Grid\nagain\nNonExistent\n");

        searchService.searchAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Nobody has been found!"));
    }

    @Test
    @DisplayName("Should print warning when sub-action text is completely incorrect")
    void testSearchActionWrongActionText() {
        contacts.add(new Organization("Grid Dynamics", "123", "Kharkiv"));
        // search 'Grid' -> invalid option 'hello' -> exit via 'back'
        Scanner scanner = createMockScanner("Grid\nhello\nback\n");

        searchService.searchAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Wrong action!"));
    }

    @Test
    @DisplayName("Should handle BadIndexException gracefully when index is out of bounds")
    void testSearchActionWrongIndex() {
        contacts.add(new Organization("Grid Dynamics", "123", "Kharkiv"));
        // search 'Grid' -> invalid numeric index '99' -> exit via 'back'
        Scanner scanner = createMockScanner("Grid\n99\nback\n");

        searchService.searchAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Wrong index! Try again."));
    }

    @Test
    @DisplayName("Should navigate to editActions and handle menu response when valid index is chosen")
    void testSearchActionValidIndex() {
        contacts.add(new Organization("Grid Dynamics", "123", "Kharkiv"));
        // earch 'Grid' -> choose index '1' -> exit edit mode immediately via 'menu'
        Scanner scanner = createMockScanner("Grid\n1\nmenu\n");

        searchService.searchAction(contacts, scanner);

        String output = outputStreamCaptor.toString();
        assertAll("Verify text logs for deep navigation",
                () -> assertTrue(output.contains("Organization name: Grid Dynamics")),
                () -> assertTrue(output.contains("Address: Kharkiv"))
        );
    }
}