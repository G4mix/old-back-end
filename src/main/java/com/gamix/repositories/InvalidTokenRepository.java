package com.gamix.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.gamix.models.InvalidToken;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Integer> {
    Optional<InvalidToken> findByToken(String token);

    @Transactional
    void deleteByToken(String token);

    @Query("SELECT p FROM InvalidToken p WHERE p.bannedUntil IS NOT NULL AND p.bannedUntil < CURRENT_TIMESTAMP")
    List<InvalidToken> findTokensToUnbanNow();

    @Query("SELECT p FROM InvalidToken p WHERE p.bannedUntil IS NOT NULL")
    List<InvalidToken> findTokensToUnbanSoon();
}
