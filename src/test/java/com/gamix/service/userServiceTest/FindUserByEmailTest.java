package com.gamix.service.userServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.user.UserNotFoundByEmail;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class FindUserByEmailTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindUserByEmail() throws ExceptionBase {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByEmail("test@example.com");

        assertEquals(user, foundUser);
    }

    @Test
    public void testFindUserByEmailNotFound() throws ExceptionBase {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundByEmail.class, () -> {
            userService.findUserByEmail("nonexistent@example.com");
        });
    }
}
