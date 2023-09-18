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
public class FindUserByEmail {
    @Autowired
    private UserService userService;
    
    @MockBean
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
