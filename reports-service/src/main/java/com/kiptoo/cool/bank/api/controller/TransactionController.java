package com.kiptoo.cool.bank.api.controller;

import com.kiptoo.cool.bank.api.exception.TransactionException;
import com.kiptoo.cool.bank.api.model.Transaction;
import com.kiptoo.cool.bank.api.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> createDeposit(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description,
            @RequestParam Long userId) {
        Transaction transaction = transactionService.createDeposit(accountId, amount, description, userId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> createWithdrawal(
            @RequestParam Long accountId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description,
            @RequestParam Long userId) throws TransactionException {
        Transaction transaction = transactionService.createWithdrawal(accountId, amount, description, userId);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @RequestParam Long sourceAccountId,
            @RequestParam Long destinationAccountId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description,
            @RequestParam Long userId) throws TransactionException {
        Transaction transaction = transactionService.transfer(
                sourceAccountId, destinationAccountId, amount, description, userId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<List<Transaction>> getAccountTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getAccountTransactions(accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) {
        List<Transaction> transactions = transactionService.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{reference}")
    public ResponseEntity<Transaction> getTransactionByReference(@PathVariable String reference) {
        Transaction transaction = transactionService.getTransactionByReference(reference);
        return ResponseEntity.ok(transaction);
    }
}