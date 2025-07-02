package com.kiptoo.cool.bank.api.repository;

import com.kiptoo.cool.bank.api.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByReportReference(String reportReference);

    @Query("SELECT r FROM Report r WHERE r.user.id = :userId ORDER BY r.generatedAt DESC")
    List<Report> findByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Report r WHERE r.account.id = :accountId AND r.reportType = 'ACCOUNT_STATEMENT' ORDER BY r.generatedAt DESC")
    List<Report> findAccountStatements(@Param("accountId") Long accountId);

    @Query(value = """
           SELECT * FROM reports 
           WHERE generated_at BETWEEN :startDate AND :endDate
           AND report_type = :reportType
           ORDER BY generated_at DESC
           """, nativeQuery = true)
    List<Report> findReportsByTypeAndDateRange(
            @Param("reportType") String reportType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}