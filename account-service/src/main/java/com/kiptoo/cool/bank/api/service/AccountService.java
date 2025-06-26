package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.dto.AccountRegistrationRequest;
import com.kiptoo.cool.bank.api.dto.AccountRegistrationResponse;
import com.kiptoo.cool.bank.api.model.Account;
import com.kiptoo.cool.bank.api.model.User;
import com.kiptoo.cool.bank.api.repository.AccountRepository;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private static final String USER_NOT_FOUND_MESSAGE = "User not found with ID: ";
    private static final String ACCOUNT_EXISTS_MESSAGE = "Account number already exists";
    private static final String ACCOUNT_NOT_FOUND_MESSAGE = "Account not found";

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountRegistrationResponse registerAccount(AccountRegistrationRequest request) {
        try {
            log.info("Registering account for user ID: {} with account number: {}",
                    request.userId(), request.accountNumber());

            return validateAndCreateAccount(request);
        } catch (Exception e) {
            log.error("Error registering account: {}", e.getMessage(), e);
            return AccountRegistrationResponse.error("Failed to register account: " + e.getMessage());
        }
    }

    public AccountRegistrationResponse getAccountByNumber(String accountNumber) {
        try {
            Optional<Account> accountOptional = accountRepository.findByAccountNumber(accountNumber);
            if (accountOptional.isEmpty()) {
                return AccountRegistrationResponse.error(ACCOUNT_NOT_FOUND_MESSAGE + " with number: " + accountNumber);
            }
            return createSuccessResponse(accountOptional.get());
        } catch (Exception e) {
            log.error("Error retrieving account by number: {}", e.getMessage(), e);
            return AccountRegistrationResponse.error("Failed to retrieve account: " + e.getMessage());
        }
    }

    public AccountRegistrationResponse getAccountByUserId(Long userId) {
        try {
            Optional<Account> accountOptional = accountRepository.findByUserId(userId);
            if (accountOptional.isEmpty()) {
                return AccountRegistrationResponse.error(ACCOUNT_NOT_FOUND_MESSAGE + " for user ID: " + userId);
            }
            return createSuccessResponse(accountOptional.get());
        } catch (Exception e) {
            log.error("Error retrieving account by user ID: {}", e.getMessage(), e);
            return AccountRegistrationResponse.error("Failed to retrieve account: " + e.getMessage());
        }
    }

    private AccountRegistrationResponse validateAndCreateAccount(AccountRegistrationRequest request) {
        // Validate user exists
        Optional<User> userOptional = validateAndGetUser(request.userId());
        if (userOptional.isEmpty()) {
            return AccountRegistrationResponse.error(USER_NOT_FOUND_MESSAGE + request.userId());
        }

        User user = userOptional.get();

        // Check if account number already exists
        if (isAccountNumberExists(request.accountNumber())) {
            return AccountRegistrationResponse.error(ACCOUNT_EXISTS_MESSAGE);
        }

        // Create and save account
        Account savedAccount = createAndSaveAccount(request, user);
        return createSuccessResponse(savedAccount);
    }

    private Optional<User> validateAndGetUser(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return userRepository.findById(userId);
    }

    private boolean isAccountNumberExists(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return false;
        }
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    private Account createAndSaveAccount(AccountRegistrationRequest request, User user) {
        Account newAccount = buildAccount(request, user);
        return accountRepository.save(newAccount);
    }

    private Account buildAccount(AccountRegistrationRequest request, User user) {
        LocalDateTime now = LocalDateTime.now();

        return Account.builder()
                .accountNumber(request.accountNumber())
                .accountType(request.accountType())
                .balance(request.initialBalance())
                .isActive(true)
                .user(user)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    private AccountRegistrationResponse createSuccessResponse(Account savedAccount) {
        AccountRegistrationResponse success = AccountRegistrationResponse.success(
                savedAccount.getId(),
                savedAccount.getAccountNumber(),
                savedAccount.getAccountType(),
                savedAccount.getBalance(),
                savedAccount.getIsActive(),
                savedAccount.getCreatedAt(),
                savedAccount.getUser().getId()
        );
        return success;
    }
}