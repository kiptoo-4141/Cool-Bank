package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.dto.LoginRequest;
import com.kiptoo.cool.bank.api.dto.LoginResponse;
import com.kiptoo.cool.bank.api.model.User;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import com.kiptoo.cool.bank.api.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.email());

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());
        String fullName = user.getFirstName() + " " + user.getLastName();

        return new LoginResponse(
                token,
                user.getEmail(),
                fullName,
                "Login successful"
        );
    }
}