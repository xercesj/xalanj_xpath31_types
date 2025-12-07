package xml.xpath31.processor.types;

import org.apache.xpath.objects.ResultSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.transform.TransformerException;

import java.net.URI;

import static org.apache.xpath.objects.XObject.CLASS_XS_ANY_URI;
import static org.junit.jupiter.api.Assertions.*;

class XSAnyURITest {

    @ParameterizedTest
    @ValueSource(strings = {
            "domain.tld",
            "https://domain.tld",
            "http://[2001:db8::1]/",
            "mailto:java-net@java.sun.com",
            "news:comp.lang.java",
            "urn:isbn:096139210x",
            "file:///foo/bar",
            "../../relative/path"
    })
    void validUriStrings(final String value) {
        // ensure XSAnyURI can be constructed with valid URI syntax
        // Currently any string is supported but ideally a URISyntaxException should be thrown on bad values
        new XSAnyURI(value);
        new XSAnyURI(URI.create(value));
    }

    @Test
    void constructor() throws TransformerException {
        final ResultSequence input = new ResultSequence();
        input.add(new XSAnyURI("https://website.tld"));

        final ResultSequence sequence = new XSAnyURI().constructor(input);

        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals("https://website.tld", ((XSAnyURI)sequence.item(0)).stringValue());
    }

    @Test
    void typeName() {
        assertEquals("anyURI", new XSAnyURI().typeName());
    }

    @Test
    void stringType() {
        assertEquals("xs:anyURI", new XSAnyURI().stringType());
    }

    @Test
    void stringValue() {
        assertEquals("https://website.tld", new XSAnyURI("https://website.tld").stringValue());
    }

    @Test
    void testEquals() {
        assertTrue(new XSAnyURI("https://website.tld").equals(new XSAnyURI("https://website.tld")));
    }

    @Test
    void eq() throws TransformerException {
        assertTrue(new XSAnyURI("https://website.tld").eq(new XSAnyURI("https://website.tld")));
    }

    @Test
    void lt() throws TransformerException {
        assertTrue(new XSAnyURI("https://aaaa.domain.tld").lt(new XSAnyURI("https://bbbb.domain.tld")));
    }

    @Test
    void gt() throws TransformerException {
        assertTrue(new XSAnyURI("https://bbbb.domain.tld").gt(new XSAnyURI("https://aaaa.domain.tld")));
    }

    @Test
    void getType() {
        assertEquals(CLASS_XS_ANY_URI, new XSAnyURI().getType());
    }
}