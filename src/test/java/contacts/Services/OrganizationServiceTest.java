package contacts.Services;

import contacts.Entity.Organization;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final java.io.InputStream standardIn = System.in;

    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        organizationService = new OrganizationService();
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
    @DisplayName("Should successfully prompt user and construct a valid Organization instance")
    void testNewOrganizationCreation() {
        //  name -> address -> number (invalid format to test checkNumber)
        Scanner scanner = createMockScanner("Pizza Hut\nWall Street 12\nwrong_number_here\n");

        Organization result = organizationService.newOrganization(scanner);

        String output = outputStreamCaptor.toString();

        assertAll("Verify prompt texts and created entity state",
                //Check if user prompts were displayed in correct order
                () -> assertTrue(output.contains("Enter the name:")),
                () -> assertTrue(output.contains("Enter the address:")),
                () -> assertTrue(output.contains("Enter the number:")),

                //Check fields of the created object
                () -> assertNotNull(result, "Created organization should not be null"),
                () -> assertEquals("Pizza Hut", result.getName()),
                () -> assertEquals("Wall Street 12", result.getAddress()),
                //checkNumber should fallback to "[no number]" for invalid input structure
                () -> assertEquals("[no number]", result.getNumber())
        );
    }
}