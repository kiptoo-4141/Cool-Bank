package com.kiptoo.cool.bank.api.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record WithdrawalRequestDto(
        @NotNull @Positive BigDecimal amount,
        @NotNull Long accountId,
        @NotNull Long userId,
        String description
) {}
