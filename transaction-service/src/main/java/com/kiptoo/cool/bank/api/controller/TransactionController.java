package com.kiptoo.cool.bank.api.controller;

import com.kiptoo.cool.bank.api.dtos.*;
import com.kiptoo.cool.bank.api.exception.*;
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
    public ResponseEntity<TransactionResponseDto> deposit(@Valid @RequestBody DepositRequestDto request) throws TransactionException {
        return ResponseEntity.ok(transactionService.processDeposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDto> withdraw(@Valid @RequestBody WithdrawalRequestDto request) throws TransactionException, InsufficientBalanceException {
        return ResponseEntity.ok(transactionService.processWithdrawal(request));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDto> transfer(@Valid @RequestBody TransferRequestDto request) throws TransactionException, InsufficientBalanceException {
        return ResponseEntity.ok(transactionService.processTransfer(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getUserTransactions(@PathVariable Long userId) throws TransactionException {
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }

//    @GetMapping("/account/{accountId}")
//    public ResponseEntity<List<TransactionResponseDto>> getAccountTransactions(@PathVariable Long accountId) throws TransactionException {
//        return ResponseEntity.ok(transactionService.getAccountTransactions(accountId));
//    }

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
}