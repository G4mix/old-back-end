package com.gamix.service.PasswordUserServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

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

    @InjectMocks
    private PasswordUserService passwordUserService;

    @Test
    public void testSignInPasswordUser() throws ExceptionBase {
        // Cenário de sucesso com email
        SignInPasswordUserInput validEmailInput = new SignInPasswordUserInput(null, "validemail@gmail.com", "Password123!", false);
        User mockUserByEmail = new User();
        PasswordUser mockPasswordUser = new PasswordUser();

        // Simular BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);
        String hashedPassword = passwordEncoder.encode(validEmailInput.password());
        mockPasswordUser.setPassword(hashedPassword);
        mockUserByEmail.setPasswordUser(mockPasswordUser);

        when(userService.findUserByEmail("validemail@gmail.com")).thenReturn(mockUserByEmail);

        when(passwordEncoder.matches("Password123!", hashedPassword)).thenReturn(true);

        // Simular jwtManager
        JwtTokens mockJwtTokens = new JwtTokens("accessToken", "refreshToken", false);
        when(jwtManager.generateJwtTokens(mockUserByEmail.getUsername(), validEmailInput.rememberMe())).thenReturn(mockJwtTokens);

        JwtTokens tokens = passwordUserService.signInPasswordUser(validEmailInput);
        assertEquals("accessToken", tokens.accessToken());
        assertEquals("refreshToken", tokens.refreshToken());

        // Testar usuário não encontrado
        SignInPasswordUserInput invalidUserInput = new SignInPasswordUserInput(null, "invalidemail@gmail.com", "Password123!", false);
        when(userService.findUserByEmail("invalidemail@gmail.com")).thenReturn(null);

        assertThrows(UserNotFound.class, () -> {
            passwordUserService.signInPasswordUser(invalidUserInput);
        });

        // Testar PasswordUser não encontrado
        User mockUserByEmailWithoutPasswordUser = new User();
        when(userService.findUserByEmail("noPasswordUser@gmail.com")).thenReturn(mockUserByEmailWithoutPasswordUser);

        assertThrows(PasswordUserNotFound.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "noPasswordUser@gmail.com", "Password123!", false));
        });

        // Testar ExcessiveFailedLoginAttempts
        User mockUserWithBlockedPasswordUser = new User();
        PasswordUser mockBlockedPasswordUser = new PasswordUser();
        mockBlockedPasswordUser.setLoginAttempts(3);
        mockBlockedPasswordUser.setBlockedUntil(LocalDateTime.now().plusMinutes(30));
        mockUserWithBlockedPasswordUser.setPasswordUser(mockBlockedPasswordUser);
        when(userService.findUserByEmail("blockedUser@gmail.com")).thenReturn(mockUserWithBlockedPasswordUser);

        assertThrows(ExcessiveFailedLoginAttempts.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "blockedUser@gmail.com", "WrongPassword", false));
        });

        // Testar PasswordWrong
        PasswordUser mockPasswordUserWrongPassword = new PasswordUser();
        when(passwordEncoder.matches("WrongPassword", mockPasswordUserWrongPassword.getPassword())).thenReturn(false);
        User mockUserWrongPassword = new User();
        mockUserWrongPassword.setPasswordUser(mockPasswordUserWrongPassword);
        when(userService.findUserByEmail("wrongPassword@gmail.com")).thenReturn(mockUserWrongPassword);

        assertThrows(PasswordWrong.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "wrongPassword@gmail.com", "WrongPassword", false));
        });

        // Testar NullJwtTokens
        User mockUserWithNullJwt = new User();
        PasswordUser mockPasswordUserNullJwt = new PasswordUser();
        when(passwordEncoder.matches("Password123!", mockPasswordUserNullJwt.getPassword())).thenReturn(true);
        mockUserWithNullJwt.setPasswordUser(mockPasswordUserNullJwt);
        when(userService.findUserByEmail("nullJwt@gmail.com")).thenReturn(mockUserWithNullJwt);
        when(jwtManager.generateJwtTokens("nullJwt", false)).thenReturn(null);

        assertThrows(NullJwtTokens.class, () -> {
            passwordUserService.signInPasswordUser(new SignInPasswordUserInput(null, "nullJwt@gmail.com", "Password123!", false));
        });
    }
}
