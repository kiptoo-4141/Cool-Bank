package com.kiptoo.cool.bank.api.dtos;

import com.kiptoo.cool.bank.api.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Response DTO for transaction operations
public record TransactionResponseDto(
        String transactionReference,
        BigDecimal amount,
        Transaction.TransactionType transactionType,
        Transaction.TransactionStatus status,
        String description,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long sourceAccountId,
        Long destinationAccountId,
        Long userId
) {
    public static TransactionResponseDto fromTransaction(Transaction transaction) {
        return new TransactionResponseDto(
                transaction.getTransactionReference(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCreatedAt(),
                transaction.getUpdatedAt(),
                transaction.getSourceAccount() != null ? transaction.getSourceAccount().getId() : null,
                transaction.getDestinationAccount() != null ? transaction.getDestinationAccount().getId() : null,
                transaction.getUser().getId()
        );
    }
}
