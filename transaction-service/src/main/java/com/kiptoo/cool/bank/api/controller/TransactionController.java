package com.kiptoo.cool.bank.api.controller;

import com.kiptoo.cool.bank.api.dtos.TransactionRequestDto;
import com.kiptoo.cool.bank.api.dtos.TransactionResponseDto;
import com.kiptoo.cool.bank.api.exception.InsufficientBalanceException;
import com.kiptoo.cool.bank.api.exception.TransactionException;
import com.kiptoo.cool.bank.api.model.Transaction;
import com.kiptoo.cool.bank.api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody TransactionRequestDto request) throws TransactionException {
        return ResponseEntity.ok(transactionService.createDeposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@Valid @RequestBody TransactionRequestDto request) throws TransactionException, InsufficientBalanceException {
        return ResponseEntity.ok(transactionService.createWithdrawal(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transfer(@Valid @RequestBody TransactionRequestDto request) throws TransactionException, InsufficientBalanceException {
        return ResponseEntity.ok(TransactionResponseDto.fromTransaction(transactionService.createTransfer(request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable Long userId) throws TransactionException {
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDto>> getAccountTransactions(@PathVariable Long accountId) throws TransactionException {
        return ResponseEntity.ok(transactionService.getAccountTransactions(accountId));
    }

    @GetMapping("/account/{accountId}/history")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionHistory(
            @PathVariable Long accountId,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) throws TransactionException {
        return ResponseEntity.ok(transactionService.getTransactionsBetweenDates(accountId, startDate, endDate));
    }

//    @GetMapping("/{reference}")
//    public ResponseEntity<TransactionResponseDto> getTransactionByReference(
//            @PathVariable String reference) {
//        return ResponseEntity.ok(transactionService.getTransactionByReference(reference));
//    }
//
//    @GetMapping("/account/{accountId}/type/{type}")
//    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByType(
//            @PathVariable Long accountId,
//            @PathVariable String type) {
//        return ResponseEntity.ok(transactionService.getTransactionsByType(accountId, type));
//    }
}