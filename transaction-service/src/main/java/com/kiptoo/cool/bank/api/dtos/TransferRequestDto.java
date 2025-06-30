package com.kiptoo.cool.bank.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull Long sourceAccountId,
        @NotNull Long destinationAccountId,
        @NotNull Long userId,
        String description
) {}
