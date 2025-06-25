package com.kiptoo.cool.bank.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private String email;
    private String fullName;
    private String message;

    public LoginResponse(String token, String email, String fullName, String message) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
        this.message = message;
    }
}