package com.kiptoo.cool.bank.api.dto;

import com.kiptoo.cool.bank.api.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRegistrationResponse {

    private boolean success;
    private String message;
    private String errorCode;

    // Account details (only populated on success)
    private Long accountId;
    private String accountNumber;
    private String accountType;
    private Double balance;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private Long userId;

    /**
     * Check if the response indicates a successful operation
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Create a successful response with account details
     */
    public static AccountRegistrationResponse success(
            Long accountId,
            String accountNumber,
            Account.AccountType accountType,
            BigDecimal balance,
            Boolean isActive,
            LocalDateTime createdAt,
            Long userId) {

        return AccountRegistrationResponse.builder()
                .success(true)
                .message("Account registered successfully")
                .accountId(accountId)
                .accountNumber(accountNumber)
                .accountType(accountType.name()) // Convert enum to String
                .balance(balance.doubleValue()) // Convert BigDecimal to Double
                .isActive(isActive)
                .createdAt(createdAt)
                .userId(userId)
                .build();
    }

    /**
     * Create an error response with error message
     */
    public static AccountRegistrationResponse error(String errorMessage) {
        return AccountRegistrationResponse.builder()
                .success(false)
                .message(errorMessage)
                .build();
    }

    /**
     * Create an error response with error message and error code
     */
    public static AccountRegistrationResponse error(String errorMessage, String errorCode) {
        return AccountRegistrationResponse.builder()
                .success(false)
                .message(errorMessage)
                .errorCode(errorCode)
                .build();
    }

    /**
     * Create a successful response for account retrieval
     */
    public static AccountRegistrationResponse successWithAccount(
            Long accountId,
            String accountNumber,
            String accountType,
            Double balance,
            Boolean isActive,
            LocalDateTime createdAt,
            Long userId,
            String message) {

        return AccountRegistrationResponse.builder()
                .success(true)
                .message(message)
                .accountId(accountId)
                .accountNumber(accountNumber)
                .accountType(accountType)
                .balance(balance)
                .isActive(isActive)
                .createdAt(createdAt)
                .userId(userId)
                .build();
    }
}