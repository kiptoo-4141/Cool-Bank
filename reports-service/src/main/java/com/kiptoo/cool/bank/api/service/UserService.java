package com.kiptoo.cool.bank.api.service;

import com.kiptoo.cool.bank.api.exception.ResourceNotFoundException;
import com.kiptoo.cool.bank.api.model.User;
import com.kiptoo.cool.bank.api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalStateException("Email already in use");
        }
        return userRepository.save(user);
    }

    public User getUserById(Long userId) throws ResourceNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public User getUserWithAccounts(Long userId) throws ResourceNotFoundException {
        return userRepository.findByIdWithAccounts(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long userId, User userDetails) throws ResourceNotFoundException {
        User user = getUserById(userId);
        user.setFirstName(userDetails.getFirstName());
        user.setMiddleName(userDetails.getMiddleName());
        user.setLastName(userDetails.getLastName());
        // Note: Email and password updates should be handled separately with proper validation
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) throws ResourceNotFoundException {
        User user = getUserById(userId);
        userRepository.delete(user);
    }
}