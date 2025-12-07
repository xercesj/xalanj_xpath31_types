package xml.xpath31.processor.types;

import org.apache.xpath.objects.ResultSequence;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.apache.xpath.objects.XObject.CLASS_XS_DECIMAL;
import static org.junit.jupiter.api.Assertions.*;

class XSDecimalTest {

    @Test
    void stringType() {
        assertEquals("xs:decimal", new XSDecimal().stringType());
    }

    @Test
    void typeName() {
        assertEquals("decimal", new XSDecimal().typeName());
    }

    @Test
    void stringValue() {
        new XSDecimal("1.3").stringValue();
    }

    @Test
    void constructor() {
        final ResultSequence input = new ResultSequence();
        input.add(new XSDecimal("1.3"));

        final ResultSequence sequence = new XSDecimal().constructor(input);

        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals("1.3", ((XSDecimal)sequence.item(0)).stringValue());
    }

    @Test
    void zero() {
        assertFalse(new XSDecimal("1.3").zero());
    }

    @Test
    void doubleValue() {
        assertEquals(1.3, new XSDecimal("1.3").doubleValue());
    }

    @Test
    void getValue() {
        assertEquals(BigDecimal.valueOf(1.3), new XSDecimal("1.3").getValue());
    }

    @Test
    void testEquals() {
        assertTrue(new XSDecimal("1.3").equals(new XSDecimal("1.3")));
        assertTrue(new XSDecimal("1337.890625").equals(new XSDecimal("1337.890625")));

        assertFalse(new XSDecimal("0.1").equals(new XSDecimal("0.11111111111")));
        assertFalse(new XSDecimal("1.5").equals(new XSDecimal("15")));
        assertFalse(new XSDecimal("8.6").equals(new XSDecimal("65646.874")));
        assertFalse(new XSDecimal("256.684").equals(new XSDecimal("68.96864")));
    }

    @Test
    void lt() {
        assertTrue(new XSDecimal("1.3").lt(new XSDecimal("2")));
        assertTrue(new XSDecimal("1.3").lt(new XSDecimal("1.4")));
        assertTrue(new XSDecimal("1.3").lt(new XSDecimal("1.3000000000000000000001")));
    }

    @Test
    void gt() {
        assertTrue(new XSDecimal("1.3").gt(new XSDecimal("1")));
        assertTrue(new XSDecimal("1.3").gt(new XSDecimal("1.2")));
        assertTrue(new XSDecimal("1.3").gt(new XSDecimal("1.2999999999999999")));
    }

    @Test
    void getType() {
        assertEquals(CLASS_XS_DECIMAL, new XSDecimal().getType());
    }
}