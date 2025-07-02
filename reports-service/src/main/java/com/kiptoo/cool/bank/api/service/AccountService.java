package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.exception.InsufficientBalanceException;
import com.kiptoo.cool.bank.api.exception.ResourceNotFoundException;
import com.kiptoo.cool.bank.api.model.Account;
import com.kiptoo.cool.bank.api.model.User;
import com.kiptoo.cool.bank.api.repository.AccountRepository;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(Account account, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow();

        user.addAccount(account);
        return accountRepository.save(account);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow();
    }

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow();
    }

    public List<Account> getUserAccounts(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account deposit(Long accountId, BigDecimal amount) {
        Account account = getAccountById(accountId);
        account.deposit(amount);
        return accountRepository.save(account);
    }

    public Account withdraw(Long accountId, BigDecimal amount) throws InsufficientBalanceException {
        Account account = getAccountById(accountId);
        if (!account.withdraw(amount)) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        return accountRepository.save(account);
    }

    public void deactivateAccount(Long accountId) {
        Account account = getAccountById(accountId);
        account.setIsActive(false);
        accountRepository.save(account);
    }
}