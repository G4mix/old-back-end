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
public class FindUserByUsername {
    @InjectMocks
    private UserService userService;
    
    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindUserByUsername() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        User foundUser = userService.findUserByUsername("testuser");

        assertEquals(user, foundUser);
    }

    @Test
    public void testFindUserByUsernameNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        User foundUser = userService.findUserByUsername("nonexistentuser");

        assertNull(foundUser);
    }
}
