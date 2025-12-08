package xml.xpath31.processor.types;

import org.apache.xpath.objects.ResultSequence;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.xml.transform.TransformerException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class XSDurationTest {

    private static final int ONE_DAY_SECONDS = 86_400;

    @Test
    void constructor() throws TransformerException {
        final ResultSequence input = new ResultSequence();
        input.add(new XSDuration(86_000));

        final ResultSequence sequence = new XSDuration().constructor(input);

        assertNotNull(sequence);
        assertEquals(1, sequence.size());
        assertEquals("PT23H53M20S", ((XSDuration)sequence.item(0)).stringValue());
    }

    @Test
    void typeName() {
        assertEquals(
                "duration",
                new XSDuration().typeName(),
                "XSDuration always returns 'duration' for this method"
        );
    }

    @Test
    void stringType() {
        assertEquals(
                "xs:duration",
                new XSDuration().stringType(),
                "XSDuration always returns 'xs:duration' for this method"
        );
    }

    @ParameterizedTest
    @MethodSource("expectedStringValues")
    void stringValue(final int seconds, final String expectedValue) {
        assertEquals(
                expectedValue,
                new XSDuration(seconds).stringValue()
        );

        if (seconds < ONE_DAY_SECONDS) {
            // Up to the one-day point, XSDuration uses the same string format as java.time.Duration
            assertEquals(
                    new XSDuration(seconds).stringValue(),
                    Duration.of(seconds, ChronoUnit.SECONDS).toString(),
                    "XSDuration should do the same string format as java.time.Duration"
            );
        }
    }

    /*
     * Test data for use with parsing. Note that once a duration reaches 1 day, the
     * java.time.Duration format will differ from that of "xs:duration"
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
    void lt() {
        final XSDuration oneMinute = new XSDuration(60);
        final XSDuration fiveMinute = new XSDuration(300);

        assertTrue(oneMinute.lt(fiveMinute));
    }

    @Test
    void gt() {
        final XSDuration oneMinute = new XSDuration(60);
        final XSDuration fiveMinute = new XSDuration(300);

        assertTrue(fiveMinute.gt(oneMinute));
    }

}