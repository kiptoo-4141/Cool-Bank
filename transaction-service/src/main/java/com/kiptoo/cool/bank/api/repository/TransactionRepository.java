package com.kiptoo.cool.bank.api.repository;

import com.kiptoo.cool.bank.api.dtos.TransactionResponseDto;
import com.kiptoo.cool.bank.api.model.Transaction;
import com.kiptoo.cool.bank.api.model.Account;
import com.kiptoo.cool.bank.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    List<Transaction> findBySourceAccount(Account account);

    List<Transaction> findByDestinationAccount(Account account);

    List<Transaction> findByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT t FROM Transaction t WHERE " +
            "(t.sourceAccount = :account OR t.destinationAccount = :account) " +
            "AND t.createdAt BETWEEN :startDate AND :endDate")
    List<TransactionResponseDto> findAccountTransactionsBetweenDates(
            @Param("account") Account account,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);

    List<Transaction> findByStatus(Transaction.TransactionStatus status);

    boolean existsByTransactionReference(String transactionReference);


    List<TransactionResponseDto> findBySourceAccountOrDestinationAccount(Account account, Account account1);




}