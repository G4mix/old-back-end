package com.gamix.initialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.models.InvalidToken;
import com.gamix.models.PasswordUser;
import com.gamix.service.InvalidTokenService;
import com.gamix.repositories.PasswordUserRepository;

@RunWith(MockitoJUnitRunner.class)
public class SystemStartupServiceTest {

    @Mock
    private PasswordUserRepository passwordUserRepository;

    @Mock
    private InvalidTokenService invalidTokenService;

    @InjectMocks
    private SystemStartupService systemStartupService;

    @Mock
    private ScheduledExecutorService scheduler;

    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void testProcessUnbannedUsers() throws Exception {
        List<PasswordUser> unbannedUsers = Arrays.asList(
            new PasswordUser().setLoginAttempts(3).setBlockedUntil(LocalDateTime.now().minusMinutes(10)),
            new PasswordUser().setLoginAttempts(3).setBlockedUntil(LocalDateTime.now().minusMinutes(5))
        );
    
        when(passwordUserRepository.findUsersToUnbanNow()).thenReturn(unbannedUsers);
    
        Method method = SystemStartupService.class.getDeclaredMethod("processUnbannedUsers");
        method.setAccessible(true);
    
        LocalDateTime blockedUntilBefore = unbannedUsers.get(0).getBlockedUntil();
        int loginAttemptsBefore = unbannedUsers.get(0).getLoginAttempts();
    
        method.invoke(systemStartupService);
    
        LocalDateTime blockedUntilAfter = unbannedUsers.get(0).getBlockedUntil();
        int loginAttemptsAfter = unbannedUsers.get(0).getLoginAttempts();
    
        assertNotEquals(blockedUntilBefore, blockedUntilAfter);
        assertNotEquals(loginAttemptsBefore, loginAttemptsAfter);
        assertEquals(null, blockedUntilAfter);
        assertEquals(0, loginAttemptsAfter);
    }    


    @Test
    public void testProcessUnbannedTokens() throws Exception {
        List<InvalidToken> unbannedTokens = Arrays.asList(
            new InvalidToken().setToken("token1").setExpirationTimeInSeconds(10L),
            new InvalidToken().setToken("token2").setExpirationTimeInSeconds(5L)
        );
    
        when(invalidTokenService.findByExpirationTimeInSecondsLessThanEqual(anyLong())).thenReturn(unbannedTokens);
    
        Method method = SystemStartupService.class.getDeclaredMethod("processUnbannedTokens");
        method.setAccessible(true);
        method.invoke(systemStartupService);
    
        verify(invalidTokenService, times(1)).deleteInvalidToken("token1");
        verify(invalidTokenService, times(1)).deleteInvalidToken("token2");
    }

    @Test
    public void testScheduleUnbanTasksForRemainingBannedUsers() throws Exception {
        LocalDateTime user1BlockedUntil = LocalDateTime.now().plusMinutes(5);
        LocalDateTime user2BlockedUntil = LocalDateTime.now().plusMinutes(10);

        List<PasswordUser> remainingBannedUsers = Arrays.asList(
            new PasswordUser().setBlockedUntil(user1BlockedUntil),
            new PasswordUser().setBlockedUntil(user2BlockedUntil)
        );

        when(passwordUserRepository.findUsersToUnbanSoon()).thenReturn(remainingBannedUsers);

        Method method = SystemStartupService.class.getDeclaredMethod("scheduleUnbanTasksForRemainingBannedUsers");
        method.setAccessible(true);
        method.invoke(systemStartupService);

        verify(scheduler, times(1))
            .schedule(any(Runnable.class), eq(299L), eq(TimeUnit.SECONDS));
        verify(scheduler, times(1))
            .schedule(any(Runnable.class), eq(599L), eq(TimeUnit.SECONDS));
    }

    @Test
    public void testScheduleUnbanTasksForRemainingBannedTokens() throws Exception {
        List<InvalidToken> remainingBannedTokens = Arrays.asList(
            new InvalidToken().setToken("token1").setExpirationTimeInSeconds(Instant.now().getEpochSecond() + 2),
            new InvalidToken().setToken("token2").setExpirationTimeInSeconds(Instant.now().getEpochSecond() + 2)
        );

        when(invalidTokenService.findAll()).thenReturn(remainingBannedTokens);

        Method method = SystemStartupService.class.getDeclaredMethod("scheduleUnbanTasksForRemainingBannedTokens");
        method.setAccessible(true);
        method.invoke(systemStartupService);

        verify(scheduler, times(2)).schedule(any(Runnable.class), eq(1L), eq(TimeUnit.SECONDS));
    }
}