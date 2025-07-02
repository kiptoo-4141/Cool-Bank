package com.kiptoo.cool.bank.api.repository;

import com.kiptoo.cool.bank.api.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findByTransactionReference(String transactionReference);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId ORDER BY t.createdAt DESC")
    List<Transaction> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.sourceAccount.accountNumber = :accountNumber OR t.destinationAccount.accountNumber = :accountNumber) " +
            "AND t.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountNumberAndDateRange(
            @Param("accountNumber") String accountNumber,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = """
           SELECT * FROM transactions 
           WHERE (source_account_id = :accountId OR destination_account_id = :accountId)
           AND status = 'COMPLETED'
           ORDER BY created_at DESC
           LIMIT :limit
           """, nativeQuery = true)
    List<Transaction> findRecentTransactionsByAccountId(
            @Param("accountId") Long accountId,
            @Param("limit") int limit);
}