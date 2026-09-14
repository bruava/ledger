package com.bruno.ledger.controller;


import com.bruno.ledger.model.Transaction;
import com.bruno.ledger.request.TransactionRequest;
import com.bruno.ledger.response.BalanceResponse;
import com.bruno.ledger.response.TransactionResponse;
import com.bruno.ledger.service.LedgerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getTransactions() {
        return ledgerService.getTransactions().stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @PostMapping("/transaction")
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = ledgerService.createTransaction(
                request.type(),
                request.amount()
        );
        return TransactionResponse.from(transaction);
    }

    @GetMapping("/balance")
    public BalanceResponse getBalance() {
        return new BalanceResponse(ledgerService.getCurrentBalance());
    }
}
