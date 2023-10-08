package com.gamix.service.PasswordUserServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.parameters.email.EmailEmpty;
import com.gamix.exceptions.parameters.email.EmailInvalidFormat;
import com.gamix.exceptions.parameters.email.EmailNull;
import com.gamix.exceptions.parameters.email.EmailTooLong;
import com.gamix.exceptions.parameters.password.PasswordMissingNumber;
import com.gamix.exceptions.parameters.password.PasswordMissingSpecialChar;
import com.gamix.exceptions.parameters.password.PasswordMissingUppercase;
import com.gamix.exceptions.parameters.password.PasswordNull;
import com.gamix.exceptions.parameters.password.PasswordTooLong;
import com.gamix.exceptions.parameters.password.PasswordTooShort;
import com.gamix.exceptions.parameters.username.UsernameEmpty;
import com.gamix.exceptions.parameters.username.UsernameInvalidFormat;
import com.gamix.exceptions.parameters.username.UsernameNull;
import com.gamix.exceptions.parameters.username.UsernameTooLong;
import com.gamix.exceptions.parameters.username.UsernameTooShort;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisEmail;
import com.gamix.exceptions.user.UserAlreadyExistsWithThisUsername;
import com.gamix.models.User;
import com.gamix.records.inputs.PasswordUserController.SignUpPasswordUserInput;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.service.PasswordUserService;
import com.gamix.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class SignUpTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordUserRepository passwordUserRepository;

    @Mock
    private JwtManager jwtManager;

    @InjectMocks
    private PasswordUserService passwordUserService;

    @Test
    public void testSignUpWithValidInput() throws ExceptionBase {
        SignUpPasswordUserInput validInput = new SignUpPasswordUserInput("username", "validemail@gmail.com", "Password123!");
        
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("validemail@gmail.com")).thenReturn(Optional.empty());

        User mockUser = new User().setId(1);
        
        when(userService.createUser("username", "validemail@gmail.com", null)).thenReturn(mockUser);
        when(jwtManager.generateJwtTokens(eq(1), any(String.class), eq(false))).thenReturn(new JwtTokens("accessToken", "refreshToken", false));
        
        JwtTokens tokens = passwordUserService.signUpPasswordUser(validInput);
        
        assertEquals("accessToken", tokens.accessToken());
    }

    @Test
    public void testSignUpWithExistingUsername() throws ExceptionBase {
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsWithThisUsername.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("existingUser", "email@gmail.com", "Password123!"));
        });
    }

    @Test
    public void testSignUpWithExistingEmail() throws ExceptionBase {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("existingEmail@gmail.com")).thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsWithThisEmail.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("nonExistingUser", "existingEmail@gmail.com", "Password123!"));
        });
    }

    @Test
    public void testSignUpWithInvalidUsername() {
        assertThrows(UsernameNull.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput(null, "password123", "email@gmail.com"));
        });

        assertThrows(UsernameEmpty.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("", "password123", "email@gmail.com"));
        });

        assertThrows(UsernameTooShort.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("ab", "password123", "email@gmail.com"));
        });

        assertThrows(UsernameTooLong.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("a".repeat(80), "password123", "email@gmail.com"));
        });

        assertThrows(UsernameInvalidFormat.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("user@name", "password123", "email@gmail.com"));
        });
    }

    @Test
    public void testSignUpWithInvalidEmail() {
        assertThrows(EmailNull.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", null, "Password123!"));
        });

        assertThrows(EmailEmpty.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "", "Password123!"));
        });

        assertThrows(EmailTooLong.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "a".repeat(330), "Password123!"));
        });

        assertThrows(EmailInvalidFormat.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "invalidemail", "Password123!"));
        });
    }

    @Test
    public void testSignUpWithInvalidPassword() {
        assertThrows(PasswordMissingNumber.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", "password"));
        });

        assertThrows(PasswordMissingSpecialChar.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", "password123"));
        });

        assertThrows(PasswordNull.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", null));
        });

        assertThrows(PasswordTooShort.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", "1234567"));
        });

        assertThrows(PasswordTooLong.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", "a".repeat(129)));
        });

        assertThrows(PasswordMissingSpecialChar.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", "password1"));
        });

        assertThrows(PasswordMissingNumber.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", "Password"));
        });

        assertThrows(PasswordMissingUppercase.class, () -> {
            passwordUserService.signUpPasswordUser(new SignUpPasswordUserInput("username", "email@gmail.com", "password1!"));
        });
    }
}