package com.bruno.ledger.service;

import com.bruno.ledger.exception.InsufficientBalanceException;
import com.bruno.ledger.model.Transaction;
import com.bruno.ledger.model.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.bruno.ledger.model.TransactionType.WITHDRAWAL;

@Service
public class LedgerService {

    private final List<Transaction> transactions = new ArrayList<>();

    public Transaction createTransaction(TransactionType type, BigDecimal amount) {
        if(type.equals(WITHDRAWAL)) {
            validateWithdrawBalance(amount);
        }

        Transaction transaction = Transaction.of(amount, type);
        transactions.add(transaction);
        return transaction;
    }

    public BigDecimal getCurrentBalance() {
        return transactions.stream()
                .reduce(BigDecimal.ZERO,
                        (balance, t) -> switch ((t.getType())) {
                            case DEPOSIT -> balance.add(t.getAmount());
                            case WITHDRAWAL -> balance.subtract(t.getAmount());
                        },
                        BigDecimal::add);
    }

    public List<Transaction> getTransactions()  {
        return List.copyOf(transactions);
    }

    private void validateWithdrawBalance(BigDecimal amount) {
        BigDecimal currentBalance = getCurrentBalance();
        if (amount.compareTo(currentBalance) > 0) {
            throw new InsufficientBalanceException("Not enough funds to withdraw");
        }
    }
}
