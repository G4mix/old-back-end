package com.gamix.service.UserServiceTest;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class FindUserByEmail {
    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindUserByEmail() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User foundUser = userService.findUserByEmail("test@example.com");

        assertEquals(user, foundUser);
    }

    @Test
    public void testFindUserByEmailNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        User foundUser = userService.findUserByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }
}
