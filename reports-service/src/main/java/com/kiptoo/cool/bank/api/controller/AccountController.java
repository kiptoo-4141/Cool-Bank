package com.kiptoo.cool.bank.api.controller;

import com.kiptoo.cool.bank.api.exception.InsufficientBalanceException;
import com.kiptoo.cool.bank.api.model.Account;
import com.kiptoo.cool.bank.api.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<Account> createAccount(
            @PathVariable Long userId,
            @Valid @RequestBody Account account) {
        Account createdAccount = accountService.createAccount(account, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getUserAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<Account> deposit(
            @PathVariable Long accountId,
            @RequestParam BigDecimal amount) {
        Account account = accountService.deposit(accountId, amount);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<Account> withdraw(
            @PathVariable Long accountId,
            @RequestParam BigDecimal amount) throws InsufficientBalanceException {
        Account account = accountService.withdraw(accountId, amount);
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{accountId}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateAccount(@PathVariable Long accountId) {
        accountService.deactivateAccount(accountId);
    }
}