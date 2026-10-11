package com.example.my_super_app.auth.repository;

import com.example.my_super_app.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM accounts WHERE username = :input OR email = :input", nativeQuery = true)
    Optional<Account> findByUsernameOrEmail(@Param("input") String input);

}
