package com.gamix.initialization;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamix.models.InvalidToken;
import com.gamix.models.PasswordUser;
import com.gamix.service.InvalidTokenService;
import com.gamix.service.PasswordUserService;

import jakarta.annotation.PostConstruct;

@Service
public class SystemStartupService {
    @Autowired
    private PasswordUserService passwordUserService;

    @Autowired
    private InvalidTokenService invalidTokenService;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void initialize() {
        processUnbannedUsers();
        processUnbannedTokens();
        scheduleUnbanTasksForRemainingBannedUsers();
        scheduleUnbanTasksForRemainingBannedTokens();
    }

    private void processUnbannedUsers() {
        List<PasswordUser> unbannedUsers = passwordUserService.findUsersToUnbanNow();

        for (PasswordUser unbannedUser : unbannedUsers) {
            passwordUserService.unbanUser(unbannedUser);
        }
    }


    private void processUnbannedTokens() {
        List<InvalidToken> unbannedTokens = invalidTokenService.findTokensToUnbanNow();
        System.out.println("--- Tokens desbanidos ---");
        System.out.println(unbannedTokens);
        System.out.println("--- Tokens desbanidos ---");
        for (InvalidToken token : unbannedTokens) {
            invalidTokenService.deleteInvalidToken(token.getToken());
        }
    }

    private void scheduleUnbanTasksForRemainingBannedUsers() {
        List<PasswordUser> remainingBannedUsers = passwordUserService.findUsersToUnbanSoon();
        
        for (PasswordUser passwordUser : remainingBannedUsers) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(currentDateTime, passwordUser.getBlockedUntil());
            long timeUntilUnban = duration.getSeconds();

            if (timeUntilUnban > 0) {
                scheduler.schedule(() -> {
                    passwordUserService.unbanUser(passwordUser);
                }, timeUntilUnban, TimeUnit.SECONDS);
            }
        }
    }
    
    private void scheduleUnbanTasksForRemainingBannedTokens() {
        List<InvalidToken> remainingBannedTokens = invalidTokenService.findTokensToUnbanSoon();
        System.out.println("--- Tokens para desbanir ---");
        System.out.println(remainingBannedTokens);
        System.out.println("--- Tokens para desbanir ---");

        for (InvalidToken bannedToken : remainingBannedTokens) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(currentDateTime, bannedToken.getBannedUntil());
            long timeUntilUnban = duration.getSeconds();
            System.out.println(timeUntilUnban);
            if (timeUntilUnban > 0) {
                scheduler.schedule(() -> {
                    invalidTokenService.deleteInvalidToken(bannedToken.getToken());
                }, timeUntilUnban, TimeUnit.SECONDS);
            }
        }
    }
}