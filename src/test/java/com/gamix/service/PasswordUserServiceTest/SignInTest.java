package com.gamix.service.PasswordUserServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.ExcessiveFailedLoginAttempts;
import com.gamix.exceptions.authentication.NullJwtTokens;
import com.gamix.exceptions.parameters.password.PasswordWrong;
import com.gamix.exceptions.passwordUser.PasswordUserNotFound;
import com.gamix.exceptions.user.UserNotFound;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.inputs.PasswordUserController.SignInPasswordUserInput;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.service.PasswordUserService;
import com.gamix.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class SignInTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtManager jwtManager;

    @Mock
    private PasswordUserRepository passwordUserRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ScheduledExecutorService executorService;

    @InjectMocks
    private PasswordUserService passwordUserService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private String hashedPassword = passwordEncoder.encode("Password123!");

    @Test
    public void testSignInPasswordUserSuccessWithUsername() throws ExceptionBase {
        SignInPasswordUserInput validUsernameInput = new SignInPasswordUserInput("validUsername", null, "Password123!", false);
        User mockUserByUsername = new User().setId(1);
        PasswordUser mockPasswordUser = new PasswordUser();
    
        mockPasswordUser.setPassword(hashedPassword);
        mockUserByUsername.setPasswordUser(mockPasswordUser);
    
        when(userRepository.findByUsername("validUsername")).thenReturn(Optional.of(mockUserByUsername));
    
        JwtTokens mockJwtTokens = new JwtTokens("accessToken", "refreshToken", false);
        when(jwtManager.generateJwtTokens(1, validUsernameInput.rememberMe())).thenReturn(mockJwtTokens);
    
        JwtTokens tokens = passwordUserService.signInPasswordUser(validUsernameInput);
    
        assertEquals("accessToken", tokens.accessToken());
        assertEquals("refreshToken", tokens.refreshToken());
    }
    
    @Test
    public void testSignInPasswordUserSuccessWithEmail() throws ExceptionBase {
        SignInPasswordUserInput validEmailInput = new SignInPasswordUserInput(null, "validemail@gmail.com", "Password123!", false);
        User mockUserByEmail = new User().setId(1);
        PasswordUser mockPasswordUser = new PasswordUser();

        mockPasswordUser.setPassword(hashedPassword);
        mockUserByEmail.setPasswordUser(mockPasswordUser);

        when(userRepository.findByEmail("validemail@gmail.com")).thenReturn(Optional.of(mockUserByEmail));

        JwtTokens mockJwtTokens = new JwtTokens("accessToken", "refreshToken", false);
        when(jwtManager.generateJwtTokens(1, validEmailInput.rememberMe())).thenReturn(mockJwtTokens);

        JwtTokens tokens = passwordUserService.signInPasswordUser(validEmailInput);
        assertEquals("accessToken", tokens.accessToken());
        assertEquals("refreshToken", tokens.refreshToken());
    }

    @Test
    public void testSignInPasswordUserNotFound() throws ExceptionBase {
        SignInPasswordUserInput invalidUserInput = new SignInPasswordUserInput(null, "invalidemail@gmail.com", "Password123!", false);
        
        when(userRepository.findByEmail("invalidemail@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFound.class, () -> {
            passwordUserService.signInPasswordUser(invalidUserInput);
        });
    }

    @Test
    public void testSignInPasswordUserPasswordUserNotFound() throws ExceptionBase {
        User mockUserByEmailWithoutPasswordUser = new User();

        when(userRepository.findByEmail("noPasswordUser@gmail.com")).thenReturn(Optional.of(mockUserByEmailWithoutPasswordUser));

        assertThrows(PasswordUserNotFound.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "noPasswordUser@gmail.com", "Password123!", false));
        });

    }

    @Test
    public void testSignInPasswordUserExcessiveFailedLoginAttempts() throws ExceptionBase, InterruptedException {
        User mockUserWithBlockedPasswordUser = new User().setId(1);
        PasswordUser mockBlockedPasswordUser = new PasswordUser();
        mockBlockedPasswordUser
            .setPassword(hashedPassword)
            .setLoginAttempts(2)
            .setBlockedUntil(null);
        mockUserWithBlockedPasswordUser.setPasswordUser(mockBlockedPasswordUser);
    
        when(userRepository.findByEmail("blockedUser@gmail.com")).thenReturn(Optional.of(mockUserWithBlockedPasswordUser));
    
        assertThrows(PasswordWrong.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "blockedUser@gmail.com", "WrongPassword", false));
        });
    
        assertThrows(ExcessiveFailedLoginAttempts.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "blockedUser@gmail.com", "Password123!", false));
        });

        LocalDateTime newBlockedUntil = LocalDateTime.now().minusMinutes(31);
        mockBlockedPasswordUser.setBlockedUntil(newBlockedUntil);
    
        JwtTokens mockJwtTokens = new JwtTokens("accessToken", "refreshToken", false);
        when(jwtManager.generateJwtTokens(1, false)).thenReturn(mockJwtTokens);

        passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "blockedUser@gmail.com", "Password123!", false));
    
        assertEquals(Integer.valueOf(0), mockBlockedPasswordUser.getLoginAttempts());
        assertNull(mockBlockedPasswordUser.getBlockedUntil());
    }
    
    @Test
    public void testSignInPasswordUserPasswordWrong() throws ExceptionBase {
        PasswordUser mockPasswordUserWrongPassword = new PasswordUser();
        mockPasswordUserWrongPassword.setPassword(hashedPassword);
        User mockUserWrongPassword = new User();
        mockUserWrongPassword.setPasswordUser(mockPasswordUserWrongPassword);

        when(userRepository.findByEmail("wrongPassword@gmail.com")).thenReturn(Optional.of(mockUserWrongPassword));

        assertThrows(PasswordWrong.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "wrongPassword@gmail.com", "WrongPassword", false));
        });
    }

    @Test
    public void testSignInPasswordUserNullJwtTokens() throws ExceptionBase {
        User mockUserWithNullJwt = new User();
        mockUserWithNullJwt.setUsername("nullJwt");
        PasswordUser mockPasswordUserNullJwt = new PasswordUser();
        mockPasswordUserNullJwt.setPassword(hashedPassword);
        mockUserWithNullJwt.setPasswordUser(mockPasswordUserNullJwt);

        when(userRepository.findByEmail("nullJwt@gmail.com")).thenReturn(Optional.of(mockUserWithNullJwt));
        when(jwtManager.generateJwtTokens(1, false)).thenReturn(null);

        assertThrows(NullJwtTokens.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "nullJwt@gmail.com", "Password123!", false));
        });
    }
}
