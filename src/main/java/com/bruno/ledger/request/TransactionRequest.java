package com.bruno.ledger.request;

import com.bruno.ledger.model.TransactionType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequest(
        @NotNull(message = "Transaction type is required")
        TransactionType type,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount needs to be greater than zero")
        @Digits(integer = 10, fraction = 2, message = "Amounts needs to have a maximum of 2 decimal cases")
        BigDecimal amount) {
}
