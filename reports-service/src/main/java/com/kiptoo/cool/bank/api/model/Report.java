package com.kiptoo.cool.bank.api.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "report_reference", unique = true, nullable = false)
    private String reportReference;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

    @Column(name = "total_transactions")
    private Integer totalTransactions;

    @Column(name = "total_deposits", precision = 19, scale = 2)
    private BigDecimal totalDeposits;

    @Column(name = "total_withdrawals", precision = 19, scale = 2)
    private BigDecimal totalWithdrawals;

    @Column(name = "total_transfers", precision = 19, scale = 2)
    private BigDecimal totalTransfers;

    @Column(name = "opening_balance", precision = 19, scale = 2)
    private BigDecimal openingBalance;

    @Column(name = "closing_balance", precision = 19, scale = 2)
    private BigDecimal closingBalance;

    @Column(name = "file_path")
    private String filePath; // For storing PDF/Excel report files

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        if (reportReference == null) {
            reportReference = "REP" + System.currentTimeMillis();
        }
    }

    public enum ReportType {
        ACCOUNT_STATEMENT("Account Statement"),
        TRANSACTION_HISTORY("Transaction History"),
        TAX_REPORT("Tax Report"),
        MONTHLY_SUMMARY("Monthly Summary"),
        ANNUAL_SUMMARY("Annual Summary"),
        CUSTOM("Custom Report");

        private final String displayName;

        ReportType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    // Helper method to set date range
    public void setDateRange(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
    }

    // Helper method to update summary information
    public void updateSummary(int totalTransactions, BigDecimal totalDeposits,
                              BigDecimal totalWithdrawals, BigDecimal totalTransfers,
                              BigDecimal openingBalance, BigDecimal closingBalance) {
        this.totalTransactions = totalTransactions;
        this.totalDeposits = totalDeposits;
        this.totalWithdrawals = totalWithdrawals;
        this.totalTransfers = totalTransfers;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
    }
}