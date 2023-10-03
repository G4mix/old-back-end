package com.gamix.initialization;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamix.models.InvalidToken;
import com.gamix.models.PasswordUser;
import com.gamix.repositories.InvalidTokenRepository;
import com.gamix.repositories.PasswordUserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class SystemStartupService {
    @Autowired
    private PasswordUserRepository passwordUserRepository;

    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void initialize() {
        processUnbannedUsers();
        processUnbannedTokens();
        scheduleUnbanTasksForRemainingBannedUsers();
        scheduleUnbanTasksForRemainingBannedTokens();
    }

    private void processUnbannedUsers() {
        List<PasswordUser> unbannedUsers = consultUnbannedUsers();

        for (PasswordUser unbannedUser : unbannedUsers) {
            unbannedUser.setBlockedUntil(null);
            unbannedUser.setLoginAttempts(0);
            passwordUserRepository.save(unbannedUser);
        }
    }

    private List<PasswordUser> consultUnbannedUsers() {
        return passwordUserRepository.findUsersToUnbanNow();
    }

    private void processUnbannedTokens() {
        List<InvalidToken> unbannedTokens = consultUnbannedTokens();

        for (InvalidToken token : unbannedTokens) {
            invalidTokenRepository.deleteByToken(token.getToken());
        }
    }

    private List<InvalidToken> consultUnbannedTokens() {
        Long currentTimestampInSeconds = System.currentTimeMillis() / 1000;
        return invalidTokenRepository.findByExpirationTimeInSecondsLessThanEqual(currentTimestampInSeconds);
    }

    private void scheduleUnbanTasksForRemainingBannedUsers() {
        List<PasswordUser> remainingBannedUsers = consultBannedUsers();
        LocalDateTime currentDateTime = LocalDateTime.now();

        for (PasswordUser passwordUser : remainingBannedUsers) {
            Duration duration = Duration.between(currentDateTime, passwordUser.getBlockedUntil());
            long timeUntilUnban = duration.getSeconds();

            if (timeUntilUnban > 0) {
                scheduler.schedule(() -> {
                    passwordUser.setBlockedUntil(null);
                    passwordUser.setLoginAttempts(0);
                    passwordUserRepository.save(passwordUser);
                }, timeUntilUnban, TimeUnit.SECONDS);
            }
        }
    }

    private List<PasswordUser> consultBannedUsers() {
        return passwordUserRepository.findUsersToUnbanSoon();
    }
    
    private void scheduleUnbanTasksForRemainingBannedTokens() {
        List<InvalidToken> remainingBannedTokens = consultBannedTokens();
        for (InvalidToken bannedToken : remainingBannedTokens) {
            long currentTimestampInSeconds = Instant.now().getEpochSecond();
            long timeUntilUnban = bannedToken.getExpirationTimeInSeconds() - currentTimestampInSeconds;

            if (timeUntilUnban > 0) {
                scheduler.schedule(() -> {
                    invalidTokenRepository.deleteByToken(bannedToken.getToken());
                }, timeUntilUnban, TimeUnit.SECONDS);
            }
        }
    }
    
    private List<InvalidToken> consultBannedTokens() {
        return invalidTokenRepository.findAll();
    }
}