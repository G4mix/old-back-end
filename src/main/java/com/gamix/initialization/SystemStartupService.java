package com.gamix.initialization;

import com.gamix.models.User;
import com.gamix.service.AuthService;
import com.gamix.utils.ThreadPool;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class SystemStartupService {
    private final AuthService authService;

    @PostConstruct
    public void initialize() {
        processUnbannedUsers();
        scheduleUnbanTasksForRemainingBannedUsers();
    }

    private void processUnbannedUsers() {
        List<User> unbannedUsers = authService.findUsersToUnbanNow();

        for (User unbannedUser : unbannedUsers) {
            authService.unbanUser(unbannedUser);
        }
    }

    private void scheduleUnbanTasksForRemainingBannedUsers() {
        List<User> remainingBannedUsers = authService.findUsersToUnbanSoon();

        for (User user : remainingBannedUsers) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(currentDateTime, user.getBlockedUntil());
            long timeUntilUnban = duration.getSeconds();

            if (timeUntilUnban > 0) {
                ThreadPool.schedule(() -> authService.unbanUser(user), timeUntilUnban, TimeUnit.SECONDS);
            }
        }
    }
}
