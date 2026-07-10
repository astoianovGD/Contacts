package contacts.Exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadIndexExceptionTest {

    @Test
    @DisplayName("Should return correct message from getMessage")
    void testGetMessage() {
        BadIndexException exception = new BadIndexException();
        assertEquals("Bad index!", exception.getMessage(), "The error message must match exactly");
    }

    @Test
    @DisplayName("Should be catchable when thrown")
    void testExceptionIsThrown() {
        assertThrows(BadIndexException.class, () -> {
            throw new BadIndexException();
        }, "The custom exception should be successfully thrown and caught");
    }
}