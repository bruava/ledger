package com.bruno.ledger.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Transaction {

    private final UUID id;
    private final BigDecimal amount;
    private final TransactionType type;
    private final Instant timestamp;

    private Transaction(UUID id, BigDecimal amount, TransactionType type, Instant timestamp) {
        Objects.requireNonNull(type, "Type must not be null");
        Objects.requireNonNull(amount, "Amount must not be null");

        validateAmount(amount);
        validatePrecision(amount);

        this.id = id;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public static Transaction of(BigDecimal amount, TransactionType type) {
        return new Transaction(UUID.randomUUID(), amount, type, Instant.now());
    }

    private static void validatePrecision(BigDecimal amount) {
        if(amount.scale() > 2) {
            throw new IllegalArgumentException("Only two decimal cases allowed");
        }
    }

    private static void validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
