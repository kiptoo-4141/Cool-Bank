package com.kiptoo.cool.bank.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// Related DTOs for different transaction operations
public record DepositRequestDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull Long accountId,
        @NotNull Long userId,
        String description
) {}
