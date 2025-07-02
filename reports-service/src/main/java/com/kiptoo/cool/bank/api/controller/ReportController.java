package com.kiptoo.cool.bank.api.controller;

import com.kiptoo.cool.bank.api.model.Report;
import com.kiptoo.cool.bank.api.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/account-statement")
    public ResponseEntity<Report> generateAccountStatement(
            @RequestParam Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam Long userId) {
        Report report = reportService.generateAccountStatement(accountId, startDate, endDate, userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Report> getReportById(@PathVariable Long reportId) {
        Report report = reportService.getReportById(reportId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Report>> getUserReports(@PathVariable Long userId) {
        List<Report> reports = reportService.getUserReports(userId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/accounts/{accountId}/statements")
    public ResponseEntity<List<Report>> getAccountStatements(@PathVariable Long accountId) {
        List<Report> statements = reportService.getAccountStatements(accountId);
        return ResponseEntity.ok(statements);
    }
}