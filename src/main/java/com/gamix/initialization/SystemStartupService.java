package com.gamix.initialization;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamix.models.PasswordUser;
import com.gamix.service.PasswordUserService;

import jakarta.annotation.PostConstruct;

@Service
public class SystemStartupService {
    @Autowired
    private PasswordUserService passwordUserService;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void initialize() {
        processUnbannedUsers();
        scheduleUnbanTasksForRemainingBannedUsers();
    }

    private void processUnbannedUsers() {
        List<PasswordUser> unbannedUsers = passwordUserService.findUsersToUnbanNow();

        for (PasswordUser unbannedUser : unbannedUsers) {
            passwordUserService.unbanUser(unbannedUser);
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
}