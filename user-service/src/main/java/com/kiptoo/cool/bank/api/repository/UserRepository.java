package com.kiptoo.cool.bank.api.repository;

import com.kiptoo.cool.bank.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
   

    User save(User user);
}
