package xml.xpath31.processor.types;

import org.apache.xpath.objects.ResultSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.xml.transform.TransformerException;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.apache.xpath.objects.XObject.CLASS_XS_DAYTIME_DURATION;
import static org.junit.jupiter.api.Assertions.*;

class XSDayTimeDurationTest {

    private static final int ONE_DAY_SECONDS = 86_400;

    @Test
    void constructor() throws TransformerException {
        final ResultSequence input = new ResultSequence();
        input.add(new XSDayTimeDuration(86_000));

        final ResultSequence sequence = new XSDayTimeDuration().constructor(input);

        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals("PT23H53M20S", ((XSDayTimeDuration)sequence.item(0)).stringValue());
    }

    @ParameterizedTest
    @MethodSource("expectedStringValues")
    void parseDayTimeDuration(final int seconds, final String stringValue) throws TransformerException {
        final XSDuration expected = new XSDayTimeDuration(seconds);
        final XSDuration parsedVal = XSDayTimeDuration.parseDayTimeDuration(stringValue);

        assertTrue(expected.equals(parsedVal));

        assertEquals(seconds, parsedVal.value());
    }

    @ParameterizedTest
    @ValueSource(strings = { "", "not a duration" })
    void parseDayTimeDurationUnsupportedFormat(final String strVal) {
        assertThrows(TransformerException.class, () -> XSDayTimeDuration.parseDayTimeDuration(strVal));
    }

    @Test
    void typeName() {
        assertEquals("dayTimeDuration", new XSDayTimeDuration().typeName());
    }

    @Test
    void stringType() {
        assertEquals("xs:dayTimeDuration", new XSDayTimeDuration().stringType());
    }

    @ParameterizedTest
    @MethodSource("expectedStringValues")
    void stringValue(final int seconds, final String expectedValue) {
        assertEquals(
                expectedValue,
                new XSDayTimeDuration(seconds).stringValue()
        );

        if (seconds < ONE_DAY_SECONDS) {
            // Up to the one-day point, XSDayTimeDuration uses the same string format as java.time.Duration
            assertEquals(
                    new XSDayTimeDuration(seconds).stringValue(),
                    Duration.of(seconds, ChronoUnit.SECONDS).toString(),
                    "XSDayTimeDuration should do the same string format as java.time.Duration"
            );
        }
    }

    /*
     * Test data for use with parsing. Note that once a duration reaches 1 day, the
     * java.time.Duration format will differ from that of "xs:duration".
     */
    private static Stream<Arguments> expectedStringValues() {
        return Stream.of(
                Arguments.of(0, "PT0S"),
                Arguments.of(12, "PT12S"),
                Arguments.of(34, "PT34S"),
                Arguments.of(51, "PT51S"),
                Arguments.of(59, "PT59S"),
                Arguments.of(60, "PT1M"),
                Arguments.of(90, "PT1M30S"),
                Arguments.of(300, "PT5M"),
                Arguments.of(500, "PT8M20S"),
                Arguments.of(3600, "PT1H"),
                Arguments.of(86_000, "PT23H53M20S"),
                Arguments.of(ONE_DAY_SECONDS, "P1D"), // java.time.Duration differs: PT24H
                Arguments.of(90_000, "P1DT1H"), // java.time.Duration differs: PT25H
                Arguments.of(93_600, "P1DT2H"), // java.time.Duration differs: PT26H
                Arguments.of(371_280, "P4DT7H8M"), // java.time.Duration differs: PT103H8M
                Arguments.of(378_500, "P4DT9H8M20S"), // java.time.Duration differs: PT105H8M20S
                Arguments.of(500_000, "P5DT18H53M20S"), // java.time.Duration differs: PT138H53M20S
                Arguments.of(604_800, "P7D") // java.time.Duration differs: PT168H
        );
    }

    @Test
    void add() {
        final XSDayTimeDuration oneMinute = new XSDayTimeDuration(60);
        final XSDayTimeDuration fiveMinute = new XSDayTimeDuration(300);

        assertEquals(360, oneMinute.add(fiveMinute).value());
    }

    @Test
    void subtract() {
        final XSDayTimeDuration oneMinute = new XSDayTimeDuration(60);
        final XSDayTimeDuration fiveMinute = new XSDayTimeDuration(300);

        assertEquals(240.0, fiveMinute.subtract(oneMinute).value());
    }

    @Test
    void mult() throws TransformerException {
        final XSDayTimeDuration oneMinute = new XSDayTimeDuration(60);

        assertEquals(120.0, oneMinute.mult(new XSInteger("2")).value());

        // Original value remains:
        assertEquals(60.0, oneMinute.value());

        assertEquals(300.0, oneMinute.mult(new XSInteger("5")).value());
    }

    @Test
    void div() throws TransformerException {
        final XSDayTimeDuration oneMinute = new XSDayTimeDuration(60);

        assertEquals(30.0, oneMinute.div(new XSInteger("2")).value());

        // Original value remains:
        assertEquals(60.0, oneMinute.value());

        assertEquals(20.0, oneMinute.div(new XSInteger("3")).value());
    }

    @Test
    void getType() {
        assertEquals(CLASS_XS_DAYTIME_DURATION, new XSDayTimeDuration().getType());
    }
}