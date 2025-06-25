package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.dto.UserRegistrationRequest;
import com.kiptoo.cool.bank.api.dto.UserRegistrationResponse;
import com.kiptoo.cool.bank.api.model.User;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.email()) != null) {
            throw new RuntimeException("Email already in use");
        }

        User user = new User();
        user.setFirstName(request.firstName());
        user.setMiddleName(request.middleName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return new UserRegistrationResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName() + " " + savedUser.getLastName(),
                LocalDateTime.now(),
                "User registered successfully"
        );
    }
}