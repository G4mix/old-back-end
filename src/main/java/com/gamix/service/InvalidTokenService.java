package com.gamix.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamix.models.InvalidToken;
import com.gamix.repositories.InvalidTokenRepository;

@Service
public class InvalidTokenService {

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void addInvalidToken(String token, Long expirationTimeInSeconds) {
        InvalidToken invalidToken = new InvalidToken();
        invalidToken.setToken(token);
        invalidToken.setBannedUntil(LocalDateTime.now().plusSeconds(expirationTimeInSeconds));
        invalidTokenRepository.save(invalidToken);

        executorService.schedule(() -> deleteInvalidToken(token), expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    public boolean isTokenOnBlacklist(String token) {
        Optional<InvalidToken> invalidToken = invalidTokenRepository.findByToken(token);
        return invalidToken.isPresent();
    }

    public void deleteInvalidToken(String token) {
        if (invalidTokenRepository.findByToken(token).isPresent()) invalidTokenRepository.deleteByToken(token);
    }

    public List<InvalidToken> findTokensToUnbanNow() {
        return invalidTokenRepository.findTokensToUnbanNow();
    }

    public List<InvalidToken> findTokensToUnbanSoon() {
        return invalidTokenRepository.findTokensToUnbanSoon();
    }

    public List<InvalidToken> findAll() {
        return invalidTokenRepository.findAll();
    }
}