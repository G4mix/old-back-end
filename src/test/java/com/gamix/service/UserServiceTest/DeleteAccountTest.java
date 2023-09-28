package com.gamix.service.UserServiceTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeleteAccountTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testDeleteAccountSuccess() throws ExceptionBase {
        int userId = 1;
        User user = new User();
        user.setId(userId);

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteAccount(userId);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testDeleteAccountUserNotFound() throws ExceptionBase {
        int nonExistentUserId = 2;

        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);

        assertThrows(UserNotFoundById.class, () -> {
            userService.deleteAccount(nonExistentUserId);
        });

        verify(userRepository).existsById(nonExistentUserId);
    }

    @Test
    public void testDeleteAccountExceptionThrown() throws ExceptionBase {
        int userId = 3;

        when(userRepository.existsById(userId)).thenReturn(true);
        doThrow(new RuntimeException("Something went wrong")).when(userRepository).deleteById(userId);

        assertThrows(RuntimeException.class, () -> {
            userService.deleteAccount(userId);
        });

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }
}
