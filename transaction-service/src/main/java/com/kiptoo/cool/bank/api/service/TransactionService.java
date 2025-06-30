package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.dtos.*;
import com.kiptoo.cool.bank.api.mapper.TransactionMapper;
import com.kiptoo.cool.bank.api.model.*;
import com.kiptoo.cool.bank.api.repository.*;
import com.kiptoo.cool.bank.api.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionResponseDto processDeposit(DepositRequestDto request) throws TransactionException {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new TransactionException("Account not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new TransactionException("User not found"));

        validateAmount(request.amount());

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .transactionType(Transaction.TransactionType.DEPOSIT)
                .description(request.description())
                .destinationAccount(account)
                .user(user)
                .build();

        account.deposit(request.amount());
        transaction.markAsCompleted();

        accountRepository.save(account);
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDto processWithdrawal(WithdrawalRequestDto request) throws TransactionException, InsufficientBalanceException {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new TransactionException("Account not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new TransactionException("User not found"));

        validateAmount(request.amount());
        validateSufficientBalance(account, request.amount());

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .transactionType(Transaction.TransactionType.WITHDRAWAL)
                .description(request.description())
                .sourceAccount(account)
                .user(user)
                .build();

        if (account.withdraw(request.amount())) {
            transaction.markAsCompleted();
        } else {
            transaction.markAsFailed("Withdrawal failed");
            throw new TransactionException("Withdrawal processing failed");
        }

        accountRepository.save(account);
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    @Transactional
    public TransactionResponseDto processTransfer(TransferRequestDto request) throws TransactionException, InsufficientBalanceException {
        Account sourceAccount = accountRepository.findById(request.sourceAccountId())
                .orElseThrow(() -> new TransactionException("Source account not found"));

        Account destinationAccount = accountRepository.findById(request.destinationAccountId())
                .orElseThrow(() -> new TransactionException("Destination account not found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new TransactionException("User not found"));

        validateAmount(request.amount());
        validateSufficientBalance(sourceAccount, request.amount());

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .transactionType(Transaction.TransactionType.TRANSFER)
                .description(request.description())
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .user(user)
                .build();

        if (sourceAccount.withdraw(request.amount())) {
            destinationAccount.deposit(request.amount());
            transaction.markAsCompleted();
        } else {
            transaction.markAsFailed("Transfer failed");
            throw new TransactionException("Transfer processing failed");
        }

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
        return transactionMapper.toDto(transactionRepository.save(transaction));
    }

    public List<TransactionResponseDto> getUserTransactions(Long userId) throws TransactionException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TransactionException("User not found"));
        return transactionRepository.findByUser(user).stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

//    public List<TransactionResponseDto> getAccountTransactions(Long accountId) throws TransactionException {
//        Account account = accountRepository.findById(accountId)
//                .orElseThrow(() -> new TransactionException("Account not found"));
//        return transactionRepository.findBySourceAccountOrDestinationAccount(account, account).stream()
//                .mapMultiToDouble(transactionMapper::toDto)
//                .collect(Collectors.toList());
//    }

    public List<TransactionResponseDto> getTransactionsBetweenDates(Long accountId, LocalDateTime startDate, LocalDateTime endDate) throws TransactionException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new TransactionException("Account not found"));
        return transactionRepository.findAccountTransactionsBetweenDates(account, startDate, endDate);
    }

//    public TransactionResponseDto getTransactionByReference(String reference) {
//        return transactionRepository.findByTransactionReference(reference)
//                .map(transaction -> transactionMapper.toDto(transaction))
//                .orElseThrow(() -> new TransactionException("Transaction not found"));
//    }

    private void validateAmount(BigDecimal amount) throws TransactionException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new TransactionException("Amount must be positive");
        }
    }

    private void validateSufficientBalance(Account account, BigDecimal amount) throws InsufficientBalanceException {
        if (!account.hasSufficientBalance(amount)) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }


}