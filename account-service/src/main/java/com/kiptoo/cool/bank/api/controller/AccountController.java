package com.kiptoo.cool.bank.api.controller;

import com.kiptoo.cool.bank.api.dto.AccountRegistrationRequest;
import com.kiptoo.cool.bank.api.dto.AccountRegistrationResponse;
import com.kiptoo.cool.bank.api.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<AccountRegistrationResponse> createAccount(
            @Valid @RequestBody AccountRegistrationRequest request) {

        AccountRegistrationResponse response = accountService.registerAccount(request);

        System.out.println("Starting Account creation");

        // Return the appropriate HTTP status based on response
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountRegistrationResponse> getAccountByNumber(
            @PathVariable String accountNumber) {
        AccountRegistrationResponse response = accountService.getAccountByNumber(accountNumber);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AccountRegistrationResponse> getAccountByUserId(
            @PathVariable Long userId) {
        AccountRegistrationResponse response = accountService.getAccountByUserId(userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}