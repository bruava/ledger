package com.bruno.ledger.controller;

import com.bruno.ledger.exception.InsufficientBalanceException;
import com.bruno.ledger.model.Transaction;
import com.bruno.ledger.model.TransactionType;
import com.bruno.ledger.service.LedgerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LedgerController.class)
class LedgerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LedgerService ledgerService;

    @Test
    void shouldCreateTransactionAndReturn201() throws Exception {
        Transaction transaction = Transaction.of(new BigDecimal("100.00"), TransactionType.DEPOSIT);
        when(ledgerService.createTransaction(any(), any())).thenReturn(transaction);

        mockMvc.perform(post("/v1/ledger/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type": "DEPOSIT", "amount": 100.00}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(100.00));
    }

    @Test
    void shouldReturn400WhenAmountIsNegative() throws Exception {
        mockMvc.perform(post("/v1/ledger/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type": "DEPOSIT", "amount": -10.00}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTypeIsMissing() throws Exception {
        mockMvc.perform(post("/v1/ledger/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"amount": 10.00}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenTypeIsInvalidEnumValue() throws Exception {
        mockMvc.perform(post("/v1/ledger/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type": "TRANSFER", "amount": 10.00}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn422WhenBalanceIsInsufficient() throws Exception {
        when(ledgerService.createTransaction(any(), any()))
                .thenThrow(new InsufficientBalanceException("Not enough funds to withdraw"));

        mockMvc.perform(post("/v1/ledger/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"type": "WITHDRAWAL", "amount": 1000.00}
                                """))
                .andExpect(status().isUnprocessableContent());
    }

    @Test
    void shouldReturnCurrentBalance() throws Exception {
        when(ledgerService.getCurrentBalance()).thenReturn(new BigDecimal("150.00"));

        mockMvc.perform(get("/v1/ledger/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150.00));
    }

    @Test
    void shouldReturnTransactionHistory() throws Exception {
        Transaction t1 = Transaction.of(new BigDecimal("100.00"), TransactionType.DEPOSIT);
        Transaction t2 = Transaction.of(new BigDecimal("30.00"), TransactionType.WITHDRAWAL);
        when(ledgerService.getTransactions()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/v1/ledger/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].type").value("DEPOSIT"))
                .andExpect(jsonPath("$[1].type").value("WITHDRAWAL"));
    }

    @Test
    void shouldReturnEmptyListWhenNoTransactions() throws Exception {
        when(ledgerService.getTransactions()).thenReturn(List.of());

        mockMvc.perform(get("/v1/ledger/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
