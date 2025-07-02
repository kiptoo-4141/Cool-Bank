package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.exception.ResourceNotFoundException;
import com.kiptoo.cool.bank.api.model.*;
import com.kiptoo.cool.bank.api.repository.AccountRepository;
import com.kiptoo.cool.bank.api.repository.ReportRepository;
import com.kiptoo.cool.bank.api.repository.TransactionRepository;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository,
                         TransactionRepository transactionRepository,
                         AccountRepository accountRepository,
                         UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Report generateAccountStatement(Long accountId, LocalDate startDate, LocalDate endDate, Long userId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow();
        User user = userRepository.findById(userId)
                .orElseThrow();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository.findByAccountNumberAndDateRange(
                account.getAccountNumber(), startDateTime, endDateTime);

        BigDecimal openingBalance = getOpeningBalance(account, startDateTime);
        BigDecimal closingBalance = account.getBalance();

        Report report = Report.builder()
                .reportType(Report.ReportType.ACCOUNT_STATEMENT)
                .title("Account Statement for " + account.getAccountNumber())
                .startDate(startDate)
                .endDate(endDate)
                .totalTransactions(transactions.size())
                .totalDeposits(calculateTotal(transactions, Transaction.TransactionType.DEPOSIT))
                .totalWithdrawals(calculateTotal(transactions, Transaction.TransactionType.WITHDRAWAL))
                .totalTransfers(calculateTotal(transactions, Transaction.TransactionType.TRANSFER))
                .openingBalance(openingBalance)
                .closingBalance(closingBalance)
                .user(user)
                .account(account)
                .build();

        return reportRepository.save(report);
    }

    private BigDecimal getOpeningBalance(Account account, LocalDateTime startDateTime) {
        // In a real application, you might want to calculate this from previous transactions
        // For simplicity, we'll use the current balance minus all transactions in the period
        List<Transaction> transactions = transactionRepository.findByAccountNumberAndDateRange(
                account.getAccountNumber(), account.getCreatedAt(), startDateTime.minusSeconds(1));

        BigDecimal netEffect = transactions.stream()
                .map(t -> {
                    if (t.getDestinationAccount() != null && t.getDestinationAccount().equals(account)) {
                        return t.getAmount();
                    } else if (t.getSourceAccount() != null && t.getSourceAccount().equals(account)) {
                        return t.getAmount().negate();
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return account.getBalance().subtract(netEffect);
    }

    private BigDecimal calculateTotal(List<Transaction> transactions, Transaction.TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Report getReportById(Long reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow();
    }

    public List<Report> getUserReports(Long userId) {
        return reportRepository.findByUserId(userId);
    }

    public List<Report> getAccountStatements(Long accountId) {
        return reportRepository.findAccountStatements(accountId);
    }
}