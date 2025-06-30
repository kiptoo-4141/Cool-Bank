package com.kiptoo.cool.bank.api.repository;

import com.kiptoo.cool.bank.api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find account by user ID
     */
    Optional<Account> findByUserId(Long userId);

    /**
     * Find account by account number and user ID
     */
    Optional<Account> findByAccountNumberAndUserId(String accountNumber, Long userId);

    /**
     * Find all accounts by user ID
     */
    List<Account> findAllByUserId(Long userId);

    /**
     * Find all active accounts by user ID
     */
    List<Account> findAllByUserIdAndIsActiveTrue(Long userId);

    /**
     * Check if account number exists
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Check if user has any account
     */
    boolean existsByUserId(Long userId);

    /**
     * Custom query to find accounts with balance greater than specified amount
     */
    @Query("SELECT a FROM Account a WHERE a.balance > :minBalance")
    List<Account> findAccountsWithBalanceGreaterThan(@Param("minBalance") BigDecimal minBalance);

    /**
     * Custom query to find active accounts by account type
     */
    @Query("SELECT a FROM Account a WHERE a.accountType = :accountType AND a.isActive = true")
    List<Account> findActiveAccountsByType(@Param("accountType") Account.AccountType accountType);
}