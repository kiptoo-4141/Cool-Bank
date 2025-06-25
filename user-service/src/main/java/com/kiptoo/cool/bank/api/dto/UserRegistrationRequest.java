package com.kiptoo.cool.bank.api.dto;

public record UserRegistrationRequest(
        String firstName,
        String middleName,
        String lastName,
        String email,
        String password
) {
}