package com.kiptoo.cool.bank.api.dtos;

import com.kiptoo.cool.bank.api.model.Transaction;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionRequestDto(
        @NotNull(message = "Transaction reference is required")
        String transactionReference,

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Transaction type is required")
        Transaction.TransactionType transactionType,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description,

        @NotNull(message = "Source account ID is required for transfers")
        Long sourceAccountId,

        @NotNull(message = "Destination account ID is required")
        Long destinationAccountId,

        @NotNull(message = "User ID is required")
        Long userId
) {
    // Additional helper methods can be added here if needed
}

// Related DTOs for different transaction operations
record DepositRequestDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull Long accountId,
        @NotNull Long userId,
        String description
) {}

record WithdrawalRequestDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull Long accountId,
        @NotNull Long userId,
        String description
) {}

record TransferRequestDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull Long sourceAccountId,
        @NotNull Long destinationAccountId,
        @NotNull Long userId,
        String description
) {}

