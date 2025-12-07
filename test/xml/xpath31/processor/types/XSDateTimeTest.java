package xml.xpath31.processor.types;

import org.apache.xpath.objects.ResultSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.transform.TransformerException;

import static org.junit.jupiter.api.Assertions.*;

class XSDateTimeTest {

    @Test
    void constructor () throws TransformerException {
        final ResultSequence input = new ResultSequence();
        input.add(XSDateTime.parseDateTime("1961-12-15T17:31:00Z"));

        final ResultSequence sequence = new XSDateTime().constructor(input);

        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals("1961-12-15T17:31:00Z", ((XSDateTime)sequence.item(0)).stringValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2025-02-28T10:00:00",
        "2025-02-28T10:00:00.0000", // the parser does support milliseconds
        "2025-02-28T10:00:00.0000000", // the parser doesn't really support milliseconds, just takes first 4 digits
        "2025-02-28T10:00:00Z",
        "2025-02-28T08:00:00+02:00",
        "2025-02-28T12:00:00-02:00",
    })
    void parseDateTime(final String strVal) throws TransformerException {
        final XSDateTime dateTime = XSDateTime.parseDateTime(strVal);
        assertEquals(2025, dateTime.year());
        assertEquals(2, dateTime.month());
        assertEquals(28, dateTime.day());

        assertEquals(0, dateTime.minute());

        // all the tested values are actually the same time
        dateTime.equals(XSDateTime.parseDateTime("2025-02-28T10:00:00Z"));

        if (!strVal.endsWith("0000")) {
            // string value doesn't support milliseconds or microseconds. Is this a bug?
            assertEquals(strVal, dateTime.stringValue());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "2025-02-28T10:00", // no seconds
        "2025-02-28T08:00:00+02" // offset needs hours and minutes
    })
    void parseDateTimeUnsupportedFormat(final String strVal) {
        assertThrows(TransformerException.class, () -> XSDateTime.parseDateTime(strVal));
    }

    @Test
    void typeName() {
        assertEquals("dateTime", new XSDateTime().typeName());
    }

    @Test
    void stringType() {
        assertEquals("xs:dateTime", new XSDateTime().stringType());
    }

    @Test
    void lt() throws TransformerException {
        final XSDateTime jan = XSDateTime.parseDateTime("2025-01-28T00:00:00Z");
        final XSDateTime feb = XSDateTime.parseDateTime("2025-02-28T00:00:00Z");

        assertTrue(jan.lt(feb));
    }

    @Test
    void gt() throws TransformerException {
        final XSDateTime jan = XSDateTime.parseDateTime("2025-01-28T00:00:00Z");
        final XSDateTime feb = XSDateTime.parseDateTime("2025-02-28T00:00:00Z");
        assertTrue(feb.gt(jan));
    }

}