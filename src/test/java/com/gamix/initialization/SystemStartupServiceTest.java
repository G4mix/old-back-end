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
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.annotation.Transactional;
import com.gamix.models.PasswordUser;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.service.PasswordUserService;

@RunWith(MockitoJUnitRunner.class)
public class SystemStartupServiceTest {

    @Mock
    private PasswordUserService passwordUserService;

    @Mock
    private PasswordUserRepository passwordUserRepository;

    @InjectMocks
    private SystemStartupService systemStartupService;

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
                new PasswordUser().setLoginAttempts(3)
                        .setBlockedUntil(LocalDateTime.now().minusMinutes(10)),
                new PasswordUser().setLoginAttempts(3)
                        .setBlockedUntil(LocalDateTime.now().minusMinutes(5)));

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
}
