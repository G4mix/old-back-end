package com.gamix.service;

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
        invalidToken.setExpirationTimeInSeconds(expirationTimeInSeconds);
        invalidTokenRepository.save(invalidToken);

        executorService.schedule(() -> invalidTokenRepository.deleteByToken(token), expirationTimeInSeconds, TimeUnit.SECONDS);
    }

    public boolean isTokenOnBlacklist(String token) {
        InvalidToken invalidToken = invalidTokenRepository.findByToken(token);
        return invalidToken != null;
    }
}