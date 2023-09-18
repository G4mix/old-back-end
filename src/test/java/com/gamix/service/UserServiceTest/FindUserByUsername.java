package com.gamix.service.UserServiceTest;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FindUserByUsername {
    @Autowired
    private UserService userService;
    
    @MockBean
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
