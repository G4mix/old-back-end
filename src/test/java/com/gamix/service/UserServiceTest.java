package com.gamix.service;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.gamix.controller.UserController;
import com.gamix.controller.UserController.UserInput;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    private UserInput userInput;

    @Before
    public void setUp() {
        userInput = new UserInput("user1", "user1@example.com", "password1", "icon1");
    }

    @Test
    public void registerPasswordUser_UserDoesNotExist() {
        configureUserDoesNotExist();

        PasswordUser createdPasswordUser = userService.registerPasswordUser(userInput);

        verify(userService).registerPasswordUser(userInput);
        assertNotNull(createdPasswordUser);
    }

    @Test
    public void registerPasswordUser_UserExistsWithoutPasswordUser() {
        configureUserExistsWithoutPasswordUser();
        
        PasswordUser createdPasswordUser = userService.registerPasswordUser(userInput);
        
        verify(userService).registerPasswordUser(userInput);
        assertEqualsPasswordUser(userInput.password(), createdPasswordUser.getPassword());
    }
    
    @Test
    public void registerPasswordUser_UserExistsWithPasswordUser() {
        configureUserExistsWithPasswordUser();
        
        PasswordUser createdPasswordUser = userService.registerPasswordUser(userInput);

        verify(userService).registerPasswordUser(userInput);
        assertNull(createdPasswordUser);
    }
    
    private void configureUserDoesNotExist() {
        when(userRepository.findByEmail(userInput.email())).thenReturn(null);
        when(userService.registerPasswordUser(userInput)).thenReturn(new PasswordUser());
    }

    private User createExistingUserWithoutPasswordUser() {
        User existingUser = new User();
        existingUser.setUsername(userInput.username());
        existingUser.setEmail(userInput.email());
        existingUser.setIcon(userInput.icon());
        return existingUser;
    }

    private void configureUserExistsWithoutPasswordUser() {
        User existingUser = createExistingUserWithoutPasswordUser();
        when(userRepository.findByEmail(userInput.email())).thenReturn(existingUser);

        PasswordUser passwordUser = new PasswordUser();
        passwordUser.setPassword(userInput.password());
        passwordUser.setUser(existingUser);
        passwordUser.setVerifiedEmail(false);
        existingUser.setPasswordUser(passwordUser);

        when(userService.createUser(userInput)).thenReturn(existingUser);
        when(userService.registerPasswordUser(userInput)).thenReturn(passwordUser);
    }

    private User createExistingUserWithPasswordUser() {
        User existingUser = createExistingUserWithoutPasswordUser();
        PasswordUser passwordUser = new PasswordUser();
        passwordUser.setPassword(userInput.password());
        passwordUser.setUser(existingUser);
        passwordUser.setVerifiedEmail(false);
        existingUser.setPasswordUser(passwordUser);
        return existingUser;
    }
    
    private void configureUserExistsWithPasswordUser() {
        User existingUser = createExistingUserWithPasswordUser();
        when(userRepository.findByEmail(userInput.email())).thenReturn(existingUser);
        when(userService.registerPasswordUser(userInput)).thenReturn(null);
    }

    private void assertEqualsPasswordUser(String expectedPassword, String actualPassword) {
        assertEquals(expectedPassword, actualPassword);
    }
}