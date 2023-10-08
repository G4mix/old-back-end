package com.gamix.initialization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
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
import org.springframework.transaction.annotation.Transactional;

import com.gamix.models.InvalidToken;
import com.gamix.models.PasswordUser;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.service.InvalidTokenService;
import com.gamix.service.PasswordUserService;

@RunWith(MockitoJUnitRunner.class)
public class SystemStartupServiceTest {

    @Mock
    private PasswordUserService passwordUserService;

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

    @Transactional
    @Test
    public void testProcessUnbannedUsers() throws Exception {
        List<PasswordUser> unbannedUsers = Arrays.asList(
            new PasswordUser().setLoginAttempts(3).setBlockedUntil(LocalDateTime.now().minusMinutes(10)),
            new PasswordUser().setLoginAttempts(3).setBlockedUntil(LocalDateTime.now().minusMinutes(5))
        );
        
        when(passwordUserService.findUsersToUnbanNow()).thenReturn(unbannedUsers);
        doAnswer(invocation -> {
            PasswordUser userToUnban = invocation.getArgument(0);

            userToUnban.setBlockedUntil(null);
            userToUnban.setLoginAttempts(0);

            passwordUserRepository.save(userToUnban);

            return userToUnban;
        }).when(passwordUserService).unbanUser(any(PasswordUser.class));

        Method method = SystemStartupService.class.getDeclaredMethod("processUnbannedUsers");
        method.setAccessible(true);
    
        LocalDateTime blockedUntilBefore = unbannedUsers.get(0).getBlockedUntil();
        int loginAttemptsBefore = unbannedUsers.get(0).getLoginAttempts();
    
        method.invoke(systemStartupService);
    
        verify(passwordUserService, times(2)).unbanUser(any(PasswordUser.class));
    
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
            new InvalidToken().setToken("token1").setBannedUntil(LocalDateTime.now().plusSeconds(10L)),
            new InvalidToken().setToken("token2").setBannedUntil(LocalDateTime.now().plusSeconds(5L))
        );
    
        when(invalidTokenService.findTokensToUnbanNow()).thenReturn(unbannedTokens);
    
        Method method = SystemStartupService.class.getDeclaredMethod("processUnbannedTokens");
        method.setAccessible(true);
        method.invoke(systemStartupService);
    
        verify(invalidTokenService, times(1)).deleteInvalidToken("token1");
        verify(invalidTokenService, times(1)).deleteInvalidToken("token2");
    }

    @Test
    public void testScheduleUnbanTasksForRemainingBannedUsers() throws Exception {
        List<PasswordUser> remainingBannedUsers = Arrays.asList(
            new PasswordUser().setBlockedUntil(LocalDateTime.now().plusMinutes(5)),
            new PasswordUser().setBlockedUntil(LocalDateTime.now().plusMinutes(10))
        );

        when(passwordUserService.findUsersToUnbanSoon()).thenReturn(remainingBannedUsers);

        Method method = SystemStartupService.class.getDeclaredMethod("scheduleUnbanTasksForRemainingBannedUsers");
        method.setAccessible(true);
        method.invoke(systemStartupService);

        verify(scheduler, times(2)).schedule(any(Runnable.class), any(Long.class), any(TimeUnit.class));
    }

    @Test
    public void testScheduleUnbanTasksForRemainingBannedTokens() throws Exception {
        List<InvalidToken> remainingBannedTokens = Arrays.asList(
            new InvalidToken().setToken("token1").setBannedUntil(LocalDateTime.now().plusSeconds(10)),
            new InvalidToken().setToken("token2").setBannedUntil(LocalDateTime.now().plusSeconds(5))
        );

        when(invalidTokenService.findTokensToUnbanSoon()).thenReturn(remainingBannedTokens);

        Method method = SystemStartupService.class.getDeclaredMethod("scheduleUnbanTasksForRemainingBannedTokens");
        method.setAccessible(true);
        method.invoke(systemStartupService);

        verify(scheduler, times(2)).schedule(any(Runnable.class), any(Long.class), any(TimeUnit.class));
    }
}