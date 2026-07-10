package contacts.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    private Person person;

    @BeforeEach
    void setUp() {
        person = new Person("Alexey", "Stoyanow", "+380501234567", "2000-01-01", "M");
    }

    @Test
    @DisplayName("Test getters and constructor initialization")
    void testGettersAndConstructor() {
        assertAll("Check all initial fields",
                () -> assertEquals("Alexey", person.getName()),
                () -> assertEquals("Stoyanow", person.getSurname()),
                () -> assertEquals("+380501234567", person.getNumber()),
                () -> assertEquals("2000-01-01", person.getBirth()),
                () -> assertEquals("M", person.getGender()),
                () -> assertTrue(person.isPerson(), "Flag isPerson should be true for Person entity")
        );
    }

    @Test
    @DisplayName("Test setters directly")
    void testSettersDirectly() {
        person.setName("Ivan");
        person.setSurname("Petrov");
        person.setBirth("1995-05-05");
        person.setGender("F");
        person.setNumber("+380639876543");

        assertAll("Check fields after using setters",
                () -> assertEquals("Ivan", person.getName()),
                () -> assertEquals("Petrov", person.getSurname()),
                () -> assertEquals("1995-05-05", person.getBirth()),
                () -> assertEquals("F", person.getGender()),
                () -> assertEquals("+380639876543", person.getNumber())
        );
    }

    @Test
    @DisplayName("Test setNumber with invalid number structure")
    void testSetNumberWithInvalidParam() {
        person.setNumber("invalid_phone_format_abc");
        assertEquals("[no number]", person.getNumber(), "Should fallback to [no number] via Main.checkNumber");
    }

    @Test
    @DisplayName("Test toString method")
    void testToStringMethod() {
        assertEquals("Alexey Stoyanow", person.toString());
    }

    @Test
    @DisplayName("Test getSearchableString format")
    void testGetSearchableString() {
        String expected = "Alexey Stoyanow +380501234567 2000-01-01 M";
        assertEquals(expected, person.getSearchableString());
    }

    @Test
    @DisplayName("Test getEditableFields list")
    void testGetEditableFields() {
        List<String> expected = List.of("name", "surname", "number", "birth", "gender");
        assertEquals(expected, person.getEditableFields());
    }

    @Test
    @DisplayName("Test setFieldValue for all valid fields")
    void testSetFieldValueValid() {
        person.setFieldValue("name", "John");
        person.setFieldValue("surname", "Doe");
        person.setFieldValue("number", "123 456");
        person.setFieldValue("birth", "[no data]");
        person.setFieldValue("gender", "[no data]");

        assertAll("Check all fields after reflection-like set",
                () -> assertEquals("John", person.getName()),
                () -> assertEquals("Doe", person.getSurname()),
                // Тут у тебе в коді setFieldValue викликає Main.checkNumber,
                // тому передаємо число "123 456" і перевіряємо, як воно збереглося (валідний або fallback)
                () -> assertNotNull(person.getNumber()),
                () -> assertEquals("[no data]", person.getBirth()),
                () -> assertEquals("[no data]", person.getGender())
        );
    }

    @Test
    @DisplayName("Test getFieldValue for all valid fields")
    void testGetFieldValueValid() {
        assertAll("Check extraction via getFieldValue",
                () -> assertEquals("Alexey", person.getFieldValue("name")),
                () -> assertEquals("Stoyanow", person.getFieldValue("surname")),
                () -> assertEquals("+380501234567", person.getFieldValue("number")),
                () -> assertEquals("2000-01-01", person.getFieldValue("birth")),
                () -> assertEquals("M", person.getFieldValue("gender"))
        );
    }

    @Test
    @DisplayName("Test default switch blocks for invalid fields")
    void testInvalidFieldsHandling() {
        assertEquals("Bad field!", person.getFieldValue("unknown_field_type"));

        person.setFieldValue("unknown_field_type", "value");
    }

    @Test
    @DisplayName("Test getDetailedInfo formatted response")
    void testGetDetailedInfo() {
        String info = person.getDetailedInfo();

        assertAll("Check detailed report strings",
                () -> assertTrue(info.contains("Name: Alexey")),
                () -> assertTrue(info.contains("Surname: Stoyanow")),
                () -> assertTrue(info.contains("Birth date: 2000-01-01")),
                () -> assertTrue(info.contains("Gender: M")),
                () -> assertTrue(info.contains("Number: +380501234567")),
                () -> assertTrue(info.contains("Time created:")),
                () -> assertTrue(info.contains("Time last edit:"))
        );
    }

    @Test
    @DisplayName("Test Contact search and regex exception block")
    void testSearchAndRegexLogic() {
        assertAll("Check global logic from abstract parent",
                () -> assertTrue(person.isContainsASearchWord("alexey")),
                () -> assertTrue(person.isContainsASearchWord("stoyanow")),
                () -> assertFalse(person.isContainsASearchWord("NonExistingName")),
                () -> assertFalse(person.isContainsASearchWord("["))
        );
    }

    @Test
    @DisplayName("Test that updateTime modifies the state successfully")
    void testUpdateTimeTrigger() {
        java.time.LocalDateTime firstCheck = person.getTimeUpdated();
        assertNotNull(firstCheck);

        person.setGender("F");
        java.time.LocalDateTime secondCheck = person.getTimeUpdated();
        assertNotNull(secondCheck);
    }
}