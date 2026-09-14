package com.bruno.ledger.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    void shouldCreateTransactionWithValidData() {

        BigDecimal amount = new BigDecimal("100.00");
        TransactionType type = TransactionType.WITHDRAWAL;

        Transaction t = Transaction.of(amount, type);
        assertNotNull(t.getId());
        assertEquals(amount, t.getAmount());
        assertEquals(type, t.getType());
        assertTrue(t.getTimestamp().isAfter(Instant.now().minusSeconds(5)));
    }

    @Test
    void shouldGenerateUniqueIdsForDifferentTransactions() {
        Transaction t1 = Transaction.of(new BigDecimal("10.00"), TransactionType.DEPOSIT);
        Transaction t2 = Transaction.of(new BigDecimal("10.00"), TransactionType.DEPOSIT);
        assertNotEquals(t1.getId(), t2.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "1.9", "1.99"})
    void shouldAcceptAmountWithMaximumTwoDecimalPlaces(String validAmount) {
        assertDoesNotThrow(() -> Transaction.of(new BigDecimal(validAmount), TransactionType.DEPOSIT));
    }

    @Test
    void shouldThrowWhenAmountIsNull() {
        assertThrows(NullPointerException.class,
                () -> Transaction.of(null, TransactionType.DEPOSIT));
    }

    @Test
    void shouldThrowWhenTypeIsNull() {
        assertThrows(NullPointerException.class,
                () -> Transaction.of(new BigDecimal("1.00"), null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1.00", "0.00", "1.999"})
    void shouldThrowForInvalidAmounts(String invalidAmount) {
        assertThrows(IllegalArgumentException.class,
                () -> Transaction.of(new BigDecimal(invalidAmount), TransactionType.DEPOSIT));
    }
}
