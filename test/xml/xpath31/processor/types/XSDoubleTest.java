package xml.xpath31.processor.types;

import org.apache.xpath.objects.ResultSequence;
import org.junit.jupiter.api.Test;

import javax.xml.transform.TransformerException;

import static org.apache.xpath.objects.XObject.CLASS_XS_DOUBLE;
import static org.junit.jupiter.api.Assertions.*;

class XSDoubleTest {

    @Test
    void parseDouble() throws TransformerException {
        assertEquals(new XSDouble("61.64"), XSDouble.parseDouble("61.64"));
        assertEquals(new XSDouble("2.03698"), XSDouble.parseDouble("2.03698"));
        assertEquals(new XSDouble("694.62"), XSDouble.parseDouble("694.62"));
    }

    @Test
    void constructor() throws TransformerException {
        final ResultSequence input = new ResultSequence();
        input.add(new XSDouble("1.3"));

        final ResultSequence sequence = new XSDouble().constructor(input);

        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals("1.3", ((XSDouble)sequence.item(0)).stringValue());
    }

    @Test
    void typeName() {
        assertEquals("double", new XSDouble().typeName());
    }

    @Test
    void stringType() {
        assertEquals("xs:double", new XSDouble().stringType());
    }

    @Test
    void stringValue() throws TransformerException {
        assertEquals("89.4", new XSDouble("89.4").stringValue());
    }

    @Test
    void negativeZero() throws TransformerException {
        assertTrue(new XSDouble("-0").negativeZero());
        assertTrue(new XSDouble("-0.0").negativeZero());
    }

    @Test
    void doubleValue() throws TransformerException {
        assertEquals(1.3, new XSDouble("1.3").doubleValue());
    }

    @Test
    void nan() {
        assertTrue(new XSDouble(Double.NaN).nan());
    }

    @Test
    void infinite() throws TransformerException {
        assertTrue(new XSDouble("INF").infinite());
        assertTrue(new XSDouble("-INF").infinite());
        assertTrue(new XSDouble(Double.POSITIVE_INFINITY).infinite());
        assertTrue(new XSDouble(Double.NEGATIVE_INFINITY).infinite());
    }

    @Test
    void zero() throws TransformerException {
        assertTrue(new XSDouble("0").zero());
        assertTrue(new XSDouble("0.0").zero());
        assertTrue(new XSDouble("0.00").zero());
    }

    @Test
    void testEquals() throws TransformerException {
        assertTrue(new XSDouble("1.3").equals(new XSDouble("1.3")));
        assertTrue(new XSDouble("1337.890625").equals(new XSDouble("1337.890625")));

        assertFalse(new XSDouble("0.1").equals(new XSDouble("0.11111111111")));
        assertFalse(new XSDouble("1.5").equals(new XSDouble("15")));
        assertFalse(new XSDouble("8.6").equals(new XSDouble("65646.874")));
        assertFalse(new XSDouble("256.684").equals(new XSDouble("68.96864")));
    }

    @Test
    void lt() throws TransformerException {
        assertTrue(new XSDouble("1.3").lt(new XSDouble("2")));
        assertTrue(new XSDouble("1.3").lt(new XSDouble("1.4")));
        assertTrue(new XSDouble("1.3").lt(new XSDouble("1.30001")));
    }

    @Test
    void gt() throws TransformerException {
        assertTrue(new XSDouble("1.3").gt(new XSDouble("1")));
        assertTrue(new XSDouble("1.3").gt(new XSDouble("1.2")));
        assertTrue(new XSDouble("1.3").gt(new XSDouble("1.2999999999999999")));
    }

    @Test
    void getType() {
        assertEquals(CLASS_XS_DOUBLE, new XSDouble().getType());
    }
}