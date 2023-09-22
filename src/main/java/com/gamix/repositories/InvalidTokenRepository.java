package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.gamix.models.InvalidToken;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Integer> {
    InvalidToken findByToken(String token);

    @Transactional
    void deleteByToken(String token);
}
