package com.kiptoo.cool.bank.api.repository;

import com.kiptoo.cool.bank.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.accounts WHERE u.id = :userId")
    Optional<User> findByIdWithAccounts(@Param("userId") Long userId);

    boolean existsByEmail(String email);
}