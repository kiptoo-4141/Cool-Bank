package com.kiptoo.cool.bank.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserRegistrationResponse {
    private Long userId;
    @Setter
    @Getter
    private String email;
    @Setter
    @Getter
    private String fullName;
    @Setter
    @Getter
    private LocalDateTime registrationDate;
    @Setter
    @Getter
    private String message;

    // Constructors
    public UserRegistrationResponse() {
    }

    public UserRegistrationResponse(Long userId, String email, String fullName, LocalDateTime registrationDate, String message) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.message = message;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}