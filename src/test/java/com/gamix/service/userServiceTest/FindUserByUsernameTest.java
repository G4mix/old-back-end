package com.gamix.service.userServiceTest;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.user.UserNotFoundByUsername;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class FindUserByUsernameTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindUserByUsername() throws ExceptionBase {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByUsername("testuser");

        assertEquals(user, foundUser);
    }

    @Test
    public void testFindUserByUsernameNotFound() throws ExceptionBase {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundByUsername.class, () -> userService.findUserByUsername("nonexistentuser"));
    }
}
