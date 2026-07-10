package contacts.Entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrganizationTest {

    Organization organization;

    @BeforeEach
    void setUp() {
        organization = new Organization("Grid Dynamics", "+065 546 4354", "Naukova avenue");
    }

    @Test
    @DisplayName("Test setter setName")
    void testMethodSetName() {
        String expected = "Pizza House";
        organization.setName(expected);
        assertEquals(expected, organization.getName());
    }

    @Test
    @DisplayName("Test getter getName")
    void testMethodGetName() {
        String expected = "Grid Dynamics";
        assertEquals(expected, organization.getName());
    }

    @Test
    @DisplayName("Test setter setAddress")
    void testMethodSetAddress() {
        String expected = "San Francisko house 10";
        organization.setAddress(expected);
        assertEquals(expected, organization.getAddress());
    }


    @Test
    @DisplayName("Test getter getAddress")
    void testMethodGetAddress() {
        String expected = "Naukova avenue";
        assertEquals(expected, organization.getAddress());
    }

    @Test
    @DisplayName("Test setter setNumber normally")
    void testMethodSetNumberNormally() {
        String expected = "+078 549 6545";
        organization.setNumber(expected);
        assertEquals(expected, organization.getNumber());
    }

    @Test
    @DisplayName("Test setter setNumber with wrong number")
    void testMethodSetNumberWithWrongParam() {
        String expected = "[no number]";
        organization.setNumber("");
        assertEquals(expected, organization.getNumber());
    }

    @Test
    @DisplayName("Test getter getName")
    void testMethodGetNumber() {
        String expected = "+065 546 4354";
        assertEquals(expected, organization.getNumber());
    }

    @Test
    @DisplayName("Test method getSearchableString")
    void testMethodGetSearchableString() {
        String expected = "Grid Dynamics Naukova avenue +065 546 4354";
        assertEquals(expected, organization.getSearchableString());
    }

    @Test
    @DisplayName("Test method getEditableFields")
    void testMethodGetEditableFields() {
        List<String> expected = List.of("name", "number", "address");
        assertEquals(expected, organization.getEditableFields());
    }

    @Test
    @DisplayName("Test method setFieldValue")
    void testMethodSetFieldValue() {
        organization.setFieldValue("name", "Bakery");
        organization.setFieldValue("number", "054 65 76 76");
        organization.setFieldValue("address", "Avenue Burmalda");

        assertAll("check all fields after setting values",
                () -> assertEquals("Bakery", organization.getName()),
                () -> assertEquals("054 65 76 76", organization.getNumber()),
                () -> assertEquals("Avenue Burmalda", organization.getAddress())
        );
    }

    @Test
    @DisplayName("Test method getFieldValue")
    void testMethodGetFieldValue() {
        String name = "Grid Dynamics", number = "+065 546 4354", address = "Naukova avenue";
        assertAll("check all fields of getFieldValue",
                () -> assertEquals(name, organization.getFieldValue("name")),
                () -> assertEquals(number, organization.getFieldValue("number")),
                () -> assertEquals(address, organization.getFieldValue("address"))
        );
    }

    @Test
    @DisplayName("Test bad fields in field value methods")
    void testBadFields() {
        assertEquals("Bad field!", organization.getFieldValue("invalid_field"));

        organization.setFieldValue("invalid_field", "some_value");
    }

    @Test
    @DisplayName("Test method getDetailedInfo")
    void testGetDetailedInfo() {
        String info = organization.getDetailedInfo();

        assertAll("check detailed info contents",
                () -> assertTrue(info.contains("Organization name: Grid Dynamics")),
                () -> assertTrue(info.contains("Address: Naukova avenue")),
                () -> assertTrue(info.contains("Number: +065 546 4354")),
                () -> assertTrue(info.contains("Time created:")),
                () -> assertTrue(info.contains("Time last edit:"))
        );
    }

    @Test
    @DisplayName("Test search word matching (Contact logic)")
    void testSearchWordMatching() {
        assertAll("check search results",
                () -> assertTrue(organization.isContainsASearchWord("grid")),
                () -> assertTrue(organization.isContainsASearchWord("Naukova")),
                () -> assertFalse(organization.isContainsASearchWord("Burmalda")),
                () -> assertFalse(organization.isContainsASearchWord("["))
        );
    }

    @Test
    @DisplayName("Test updateTime changes timeUpdated field")
    void testUpdateTimeOnModification() {
        java.time.LocalDateTime initialTime = organization.getTimeUpdated();

        organization.setName("New Name");
        java.time.LocalDateTime updatedTime = organization.getTimeUpdated();

        assertNotNull(updatedTime, "Time updated should not be null");
        assertFalse(organization.isPerson(), "Organization flag isPerson should be false");
    }
}