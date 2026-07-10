package contacts.Services;

import contacts.Entity.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;
    private final java.io.InputStream standardIn = System.in;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
        personService = new PersonService();
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
    @DisplayName("Should successfully create a Person with fully valid details")
    void testNewPersonSuccess() {
        //  name -> surname -> valid birth date -> valid gender -> valid number
        Scanner scanner = createMockScanner("Alexey\nStoyanow\n2000-05-15\nM\n+380501234567\n");

        Person result = personService.newPerson(scanner);

        String output = outputStreamCaptor.toString();

        assertAll("Verify successful prompts and correct object state",
                () -> assertTrue(output.contains("Enter the name:")),
                () -> assertTrue(output.contains("Enter the surname:")),
                () -> assertTrue(output.contains("Enter the birth date:")),
                () -> assertTrue(output.contains("Enter the gender (M, F):")),
                () -> assertTrue(output.contains("Enter the number:")),

                () -> assertNotNull(result),
                () -> assertEquals("Alexey", result.getName()),
                () -> assertEquals("Stoyanow", result.getSurname()),
                () -> assertEquals("2000-05-15", result.getBirth()),
                () -> assertEquals("M", result.getGender()),
                () -> assertEquals("+380501234567", result.getNumber())
        );
    }

    @Test
    @DisplayName("Should handle bad inputs for birth date and gender using fallbacks")
    void testNewPersonWithInvalidBirthAndGender() {
        // name -> surname -> INVALID birth -> INVALID gender -> valid number
        Scanner scanner = createMockScanner("John\nDoe\nnot-a-date\nX\n12345\n");

        Person result = personService.newPerson(scanner);

        String output = outputStreamCaptor.toString();

        assertAll("Verify error logs and fallback placeholders",
                () -> assertTrue(output.contains("Bad birth date!")),
                () -> assertTrue(output.contains("Bad gender!")),

                () -> assertNotNull(result),
                () -> assertEquals("[no data]", result.getBirth()),
                () -> assertEquals("[no data]", result.getGender())
        );
    }

    @Test
    @DisplayName("Should validate female gender successfully ignoring extra whitespace")
    void testCheckGenderFemaleWithSpaces() {
        // Testing trim and uppercase handling for gender block directly
        Scanner scanner = createMockScanner("Jane\nDoe\n1998-10-20\n  f  \n+123\n");

        Person result = personService.newPerson(scanner);

        assertAll("Verify female gender parsing",
                () -> assertNotNull(result),
                () -> assertEquals("F", result.getGender())
        );
    }
}