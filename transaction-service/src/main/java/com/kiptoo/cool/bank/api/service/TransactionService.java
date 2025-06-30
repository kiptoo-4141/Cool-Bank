package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.dtos.TransactionRequestDto;
import com.kiptoo.cool.bank.api.dtos.TransactionResponseDto;
import com.kiptoo.cool.bank.api.model.*;
import com.kiptoo.cool.bank.api.repository.TransactionRepository;
import com.kiptoo.cool.bank.api.repository.AccountRepository;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import com.kiptoo.cool.bank.api.exception.InsufficientBalanceException;
import com.kiptoo.cool.bank.api.exception.TransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public Transaction createDeposit(TransactionRequestDto request) throws TransactionException {
        Account account = accountRepository.findById(request.destinationAccountId())
                .orElseThrow(() -> new TransactionException("Destination account not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new TransactionException("User not found"));

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Deposit amount must be positive");
        }

        Transaction transaction = Transaction.builder()
                .transactionReference(request.transactionReference())
                .amount(request.amount())
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .status(Transaction.TransactionStatus.PENDING)
                .description(request.description())
                .destinationAccount(account)
                .user(user)
                .build();

        // Process deposit
        account.deposit(request.amount());
        transaction.markAsCompleted();

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction createWithdrawal(TransactionRequestDto request) throws TransactionException, InsufficientBalanceException {
        Account account = accountRepository.findById(request.sourceAccountId())
                .orElseThrow(() -> new TransactionException("Source account not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new TransactionException("User not found"));

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Withdrawal amount must be positive");
        }

        if (!account.hasSufficientBalance(request.amount())) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }

        Transaction transaction = Transaction.builder()
                .transactionReference(request.transactionReference())
                .amount(request.amount())
                .transactionType(Transaction.TransactionType.WITHDRAWAL)
                .status(Transaction.TransactionStatus.PENDING)
                .description(request.description())
                .sourceAccount(account)
                .user(user)
                .build();

        // Process withdrawal
        if (account.withdraw(request.amount())) {
            transaction.markAsCompleted();
        } else {
            transaction.markAsFailed("Withdrawal failed");
        }

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction createTransfer(TransactionRequestDto request) throws TransactionException, InsufficientBalanceException {
        Account sourceAccount = accountRepository.findById(request.sourceAccountId())
                .orElseThrow(() -> new TransactionException("Source account not found"));

        Account destinationAccount = accountRepository.findById(request.destinationAccountId())
                .orElseThrow(() -> new TransactionException("Destination account not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new TransactionException("User not found"));

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Transfer amount must be positive");
        }

        if (!sourceAccount.hasSufficientBalance(request.amount())) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }

        Transaction transaction = Transaction.builder()
                .transactionReference(request.transactionReference())
                .amount(request.amount())
                .transactionType(Transaction.TransactionType.TRANSFER)
                .status(Transaction.TransactionStatus.PENDING)
                .description(request.description())
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .user(user)
                .build();

        // Process transfer
        if (sourceAccount.withdraw(request.amount())) {
            destinationAccount.deposit(request.amount());
            transaction.markAsCompleted();
        } else {
            transaction.markAsFailed("Transfer failed");
        }

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(Long userId) throws TransactionException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TransactionException("User not found"));
        return transactionRepository.findByUser(user);
    }

    public List<TransactionResponseDto> getAccountTransactions(Long accountId) throws TransactionException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new TransactionException("Account not found"));
        return transactionRepository.findBySourceAccountOrDestinationAccount(account, account);
    }

    public List<TransactionResponseDto> getTransactionsBetweenDates(Long accountId, LocalDateTime startDate, LocalDateTime endDate) throws TransactionException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new TransactionException("Account not found"));
        return transactionRepository.findAccountTransactionsBetweenDates(account, startDate, endDate);
    }


}