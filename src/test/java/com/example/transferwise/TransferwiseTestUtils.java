package com.example.transferwise;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransferwiseTestUtils {
    // Asserts 2 big decimal values are equal but ignores precision e.g 2.0 == 2.00
    public static void assertBigDecimalValuesAreTheSame(BigDecimal firstValue, BigDecimal secondValue) {
        final String testMessage = String.format("Expected %s to be the same as %s.", firstValue.toString(), secondValue.toString());
        assertEquals(0, firstValue.compareTo(secondValue), testMessage);
    }
}
