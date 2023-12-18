import main.java.com.FSFile;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class FSEntryTest {

    @Test
    public void testNameWithoutForbiddenCharacters() {
        FSFile file = new FSFile("Text.", "ValidName12345");
        assertEquals("ValidName12345", file.getName());
    }

    @Test
    public void testNameWithForbiddenCharacters() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            FSFile file = new FSFile("Text.", "Invalid*Name");
        });
        assertEquals("The entry name contains forbidden characters!", exception.getMessage());
    }

    @Test
    public void testNameWithReservedWord() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            FSFile file = new FSFile("Text.", "COM1");
        });
        assertEquals("The entry name can't be part of the reserved words!", exception.getMessage());
    }

    @Test
    public void testNameExceedingMaxLength() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            FSFile file = new FSFile("Text.", "VeryLongNameThatExceedsMaxLength12345678901234567890123" +
                    "456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789" +
                    "012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345" +
                    "678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901" +
                    "234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                    "890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123" +
                    "4567890123456789012345678901234567890123456");
        });
        assertEquals("The entry name can't be longer than 255 characters!", exception.getMessage());
    }

    @Test
    public void testNameWithOnlyWhitespaces() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            FSFile file = new FSFile("Text.", "             ");
        });
        assertEquals("The entry name can't be made up of only whitespaces!", exception.getMessage());
    }

    @Test
    public void testNameWithInvisibleCharacter() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            FSFile file = new FSFile("Text.", "NameWithInvisibleCharacter\u0007");
        });
        assertEquals("The entry name contains forbidden characters!", exception.getMessage());
    }
}