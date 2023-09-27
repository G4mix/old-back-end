package com.gamix.service.PasswordUserServiceTest;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.records.inputs.PasswordUserController.SignOutPasswordUserInput;
import com.gamix.security.JwtManager;
import com.gamix.service.PasswordUserService;
import com.gamix.service.UserService;

import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class SignOutTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtManager jwtManager;

    @InjectMocks
    private PasswordUserService passwordUserService;

    @Test
    public void testSignOutWithValidInput() throws ExceptionBase {
        SignOutPasswordUserInput validInput = new SignOutPasswordUserInput("validAccessToken", "validRefreshToken");

        when(jwtManager.validate("validAccessToken")).thenReturn(true);
        when(jwtManager.validate("validRefreshToken")).thenReturn(true);

        Claims claims = createMockClaims("user1");

        when(jwtManager.getTokenClaims("validAccessToken")).thenReturn(claims);
        when(jwtManager.getTokenClaims("validRefreshToken")).thenReturn(claims);

        passwordUserService.signOutPasswordUser(validInput);
    }

    @Test
    public void testSignOutWithInvalidAccessToken() throws ExceptionBase {
        SignOutPasswordUserInput invalidAccessTokenInput = new SignOutPasswordUserInput("invalidAccessToken", "validRefreshToken");
        
        assertThrows(ExceptionBase.class, () -> {
            passwordUserService.signOutPasswordUser(invalidAccessTokenInput);
        });
    }

    @Test
    public void testSignOutWithInvalidRefreshToken() throws ExceptionBase {
        SignOutPasswordUserInput invalidRefreshTokenInput = new SignOutPasswordUserInput("validAccessToken", "invalidRefreshToken");

        assertThrows(ExceptionBase.class, () -> {
            passwordUserService.signOutPasswordUser(invalidRefreshTokenInput);
        });
    }

    @Test
    public void testSignOutWithMismatchedTokens() throws ExceptionBase {
        SignOutPasswordUserInput mismatchedTokensInput = new SignOutPasswordUserInput("accessToken", "refreshToken");

        when(jwtManager.validate("accessToken")).thenReturn(true);
        when(jwtManager.validate("refreshToken")).thenReturn(true);

        Claims claims1 = createMockClaims("user1");
        Claims claims2 = createMockClaims("user2");

        when(jwtManager.getTokenClaims("accessToken")).thenReturn(claims1);
        when(jwtManager.getTokenClaims("refreshToken")).thenReturn(claims2);

        assertThrows(ExceptionBase.class, () -> {
            passwordUserService.signOutPasswordUser(mismatchedTokensInput);
        });
    }

    private Claims createMockClaims(String subject) {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(subject);
        return claims;
    }

}
