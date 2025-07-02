package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.exception.ResourceNotFoundException;
import com.kiptoo.cool.bank.api.exception.TransactionException;
import com.kiptoo.cool.bank.api.model.*;
import com.kiptoo.cool.bank.api.repository.AccountRepository;
import com.kiptoo.cool.bank.api.repository.TransactionRepository;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Transaction createDeposit(Long accountId, BigDecimal amount, String description, Long userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow();
        User user = userRepository.findById(userId)
                .orElseThrow();

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .status(Transaction.TransactionStatus.COMPLETED)
                .description(description)
                .destinationAccount(account)
                .user(user)
                .build();

        account.deposit(amount);
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public Transaction createWithdrawal(Long accountId, BigDecimal amount, String description, Long userId) throws TransactionException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow();
        User user = userRepository.findById(userId)
                .orElseThrow();

        if (!account.hasSufficientBalance(amount)) {
            throw new TransactionException("Insufficient balance for withdrawal");
        }

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .transactionType(Transaction.TransactionType.WITHDRAWAL)
                .status(Transaction.TransactionStatus.COMPLETED)
                .description(description)
                .sourceAccount(account)
                .user(user)
                .build();

        account.withdraw(amount);
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public Transaction transfer(Long sourceAccountId, Long destinationAccountId,
                                BigDecimal amount, String description, Long userId) throws TransactionException {
        if (sourceAccountId.equals(destinationAccountId)) {
            throw new TransactionException("Cannot transfer to the same account");
        }

        Account sourceAccount = accountRepository.findById(sourceAccountId)
                .orElseThrow();
        Account destinationAccount = accountRepository.findById(destinationAccountId)
                .orElseThrow();
        User user = userRepository.findById(userId)
                .orElseThrow();

        if (!sourceAccount.hasSufficientBalance(amount)) {
            throw new TransactionException("Insufficient balance for transfer");
        }

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .transactionType(Transaction.TransactionType.TRANSFER)
                .status(Transaction.TransactionStatus.COMPLETED)
                .description(description)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .user(user)
                .build();

        sourceAccount.withdraw(amount);
        destinationAccount.deposit(amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAccountTransactions(Long accountId) {
        return transactionRepository.findRecentTransactionsByAccountId(accountId, 50);
    }

    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    public Transaction getTransactionByReference(String reference) {
        return transactionRepository.findByTransactionReference(reference)
                .orElseThrow();
    }
}