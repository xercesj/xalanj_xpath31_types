package xml.xpath31.processor.types;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Locale;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class XPath3DecimalFormatTest {

    private static final Locale initialLocale = Locale.getDefault();

    @BeforeAll
    static void beforeAll() {
        // the test data assumes that english is the default locale
        Locale.setDefault(Locale.ENGLISH);
    }

    @AfterAll
    static void afterAll() {
        // reset the locale
        Locale.setDefault(initialLocale);
    }

    @ParameterizedTest
    @MethodSource("formattingArgs")
    void performStrFormatting(final String pattern, final Object value, final String expectedResult) {
        assertEquals(
                expectedResult,
                new XPath3DecimalFormat(pattern).performStrFormatting(value)
        );
    }

    public static Stream<Arguments> formattingArgs() {
        return Stream.of(
                Arguments.of("#.##", 1234567.89, "1234567.89"), // double
                Arguments.of("#.##", 189.5f, "189.5"), // float
                Arguments.of("#.##", 189, "189"), // int
                Arguments.of("#.##", 189L, "189"), // long

                Arguments.of("0,000.00", 1234567.89, "1,234,567.89"), // double
                Arguments.of("0,000.00", 189.5f, "0,189.50"), // float
                Arguments.of("0,000.00", 189, "0,189.00"), // int
                Arguments.of("0,000.00", 189L, "0,189.00"), // long

                Arguments.of("#,###.##", 1234567.89, "1,234,567.89"), // double
                Arguments.of("#,###.##", 9_364.5f, "9,364.5"), // float
                Arguments.of("#,###.##", 9_364, "9,364"), // int
                Arguments.of("#,###.##", 9_364L, "9,364"), // long

                // Scientific Notation
                Arguments.of("###.000000E0", 1.23456789E6, "1.23456789E6")
        );
    }
}