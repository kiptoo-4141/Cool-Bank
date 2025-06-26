package com.kiptoo.cool.bank.api.dto;

import com.kiptoo.cool.bank.api.model.Account.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record AccountRegistrationRequest(
        @NotBlank(message = "Account number is required")
        String accountNumber,

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @PositiveOrZero(message = "Initial balance must be positive or zero")
        BigDecimal initialBalance,

        @NotNull(message = "User ID is required")
        Long userId
) {
    // Default initial balance if isn't provided
    public AccountRegistrationRequest(String accountNumber, AccountType accountType, Long userId) {
        this(accountNumber, accountType, BigDecimal.ZERO, userId);
    }
}