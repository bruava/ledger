package com.bruno.ledger.service;

import com.bruno.ledger.exception.InsufficientBalanceException;
import com.bruno.ledger.model.Transaction;
import com.bruno.ledger.model.TransactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LedgerServiceTest {

    private LedgerService ledgerService;

    @BeforeEach
    void setUp() {
        ledgerService = new LedgerService();
    }

    @Test
    void shouldReturnZeroBalanceWhenNoTransactions() {
        assertEquals(0, BigDecimal.ZERO.compareTo(ledgerService.getCurrentBalance()));
    }

    @Test
    void shouldAcceptDeposit() {
        Transaction t = ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("100.00"));

        assertNotNull(t.getId());
        assertEquals(0, new BigDecimal("100.00").compareTo(ledgerService.getCurrentBalance()));
    }

    @Test
    void shouldCalculateBalanceAfterMultipleDeposits() {
        ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("100.00"));
        ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("50.00"));

        assertEquals(0, new BigDecimal("150.00").compareTo(ledgerService.getCurrentBalance()));
    }

    @Test
    void shouldCalculateBalanceAfterDepositAndWithdrawal() {
        ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("100.00"));
        ledgerService.createTransaction(TransactionType.WITHDRAWAL, new BigDecimal("30.00"));

        assertEquals(0, new BigDecimal("70.00").compareTo(ledgerService.getCurrentBalance()));
    }

    @Test
    void shouldAllowWithdrawalEqualToCurrentBalance() {
        ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("100.00"));

        assertDoesNotThrow(() ->
                ledgerService.createTransaction(TransactionType.WITHDRAWAL, new BigDecimal("100.00")));

        assertEquals(0, BigDecimal.ZERO.compareTo(ledgerService.getCurrentBalance()));
    }

    @Test
    void shouldThrowWhenWithdrawalExceedsBalance() {
        ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("50.00"));

        assertThrows(InsufficientBalanceException.class, () ->
                ledgerService.createTransaction(TransactionType.WITHDRAWAL, new BigDecimal("50.01")));
    }

    @Test
    void shouldThrowWhenWithdrawingFromEmptyLedger() {
        assertThrows(InsufficientBalanceException.class, () ->
                ledgerService.createTransaction(TransactionType.WITHDRAWAL, new BigDecimal("10.00")));
    }

    @Test
    void shouldNotModifyBalanceWhenWithdrawalIsRejected() {
        ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("50.00"));

        assertThrows(InsufficientBalanceException.class, () ->
                ledgerService.createTransaction(TransactionType.WITHDRAWAL, new BigDecimal("100.00")));

        assertEquals(0, new BigDecimal("50.00").compareTo(ledgerService.getCurrentBalance()));
    }

    @Test
    void shouldReturnTransactionsInInsertionOrder() {
        Transaction first = ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("100.00"));
        Transaction second = ledgerService.createTransaction(TransactionType.WITHDRAWAL, new BigDecimal("30.00"));

        List<Transaction> transactions = ledgerService.getTransactions();

        assertEquals(2, transactions.size());
        assertEquals(first.getId(), transactions.get(0).getId());
        assertEquals(second.getId(), transactions.get(1).getId());
    }

    @Test
    void shouldReturnImmutableTransactionsList() {
        ledgerService.createTransaction(TransactionType.DEPOSIT, new BigDecimal("10.00"));
        List<Transaction> transactions = ledgerService.getTransactions();

        assertThrows(UnsupportedOperationException.class, () ->
                transactions.add(Transaction.of(new BigDecimal("1.00"), TransactionType.DEPOSIT)));
    }
}
