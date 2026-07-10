package contacts.Enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionTest {

    @Test
    @DisplayName("Technical test for JaCoCo to cover Action enum constants and built-in methods")
    void testActionEnumMethods() {
        Action[] actions = Action.values();
        assertEquals(5, actions.length, "Enum should have exactly 5 supported actions");

        assertAll("Check valueOf for all actions",
                () -> assertEquals(Action.ADD, Action.valueOf("ADD")),
                () -> assertEquals(Action.LIST, Action.valueOf("LIST")),
                () -> assertEquals(Action.SEARCH, Action.valueOf("SEARCH")),
                () -> assertEquals(Action.COUNT, Action.valueOf("COUNT")),
                () -> assertEquals(Action.EXIT, Action.valueOf("EXIT"))
        );
    }
}