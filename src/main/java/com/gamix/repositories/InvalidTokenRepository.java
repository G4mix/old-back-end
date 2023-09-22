package com.gamix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.gamix.models.InvalidToken;

import graphql.com.google.common.base.Optional;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, Integer> {
    Optional<InvalidToken> findByTokenValue(String tokenValue);

    @Transactional
    void deleteByTokenValue(String tokenValue);
}
