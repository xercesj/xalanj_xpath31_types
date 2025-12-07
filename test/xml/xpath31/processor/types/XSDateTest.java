package xml.xpath31.processor.types;

import org.apache.xpath.objects.ResultSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.transform.TransformerException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XSDateTest {

    @Test
    void constructor () throws TransformerException {
        final ResultSequence input = new ResultSequence();
        input.add(XSDate.parseDate("1982-10-01"));

        final ResultSequence sequence = new XSDate().constructor(input);

        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals("1982-10-01", ((XSDate)sequence.item(0)).stringValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2025-01-11",
            "2024-03-07",
            "2018-06-23",
            "1993-08-14",
            "1975-11-30"
    })
    void parseDate(final String strVal) throws TransformerException {
        final XSDate dateTime = XSDate.parseDate(strVal);

        final LocalDate localDate = LocalDate.parse(strVal);
        assertEquals(localDate.getYear(), dateTime.year());
        assertEquals(localDate.getMonth().getValue(), dateTime.month());
        assertEquals(localDate.getDayOfMonth(), dateTime.day());
    }

    @Test
    void typeName() {
        assertEquals("date", new XSDate().typeName());
    }

    @Test
    void stringType() {
        assertEquals("xs:date", new XSDate().stringType());
    }

    @Test
    void lt() throws TransformerException {
        final XSDate march = XSDate.parseDate("2000-03-21");
        final XSDate april = XSDate.parseDate("2000-04-21");
        assertTrue(march.lt(april));
    }

    @Test
    void gt() throws TransformerException {
        final XSDate march = XSDate.parseDate("2000-03-21");
        final XSDate april = XSDate.parseDate("2000-04-21");
        assertTrue(april.gt(march));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2025-01-11",
            "2024-03-07",
            "2018-06-23",
            "1993-08-14",
            "1975-11-30"
    })
    void stringValue(final String strVal) throws TransformerException {
        assertEquals(strVal, XSDate.parseDate(strVal).stringValue());
    }
}