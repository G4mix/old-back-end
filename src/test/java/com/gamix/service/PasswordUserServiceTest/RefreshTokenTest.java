package com.gamix.service.PasswordUserServiceTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidRefreshToken;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.security.JwtManager;
import com.gamix.service.PasswordUserService;

import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class RefreshTokenTest {

    @Mock
    private JwtManager jwtManager;

    @InjectMocks
    private PasswordUserService passwordUserService;

    @Test
    public void testRefreshTokenWithValidToken() throws ExceptionBase {
        String validRefreshToken = "validRefreshToken";

        when(jwtManager.validate(validRefreshToken)).thenReturn(true);

        Claims mockClaims = createMockClaims(1, true);
        when(jwtManager.getTokenClaims(validRefreshToken)).thenReturn(mockClaims);

        when(jwtManager.generateJwtTokens(1, true)).thenReturn(new JwtTokens("newAccessToken", "newRefreshToken", true));

        JwtTokens tokens = passwordUserService.refreshToken(validRefreshToken);

        assertEquals("newAccessToken", tokens.accessToken());
        assertEquals("newRefreshToken", tokens.refreshToken());
    }

    @Test
    public void testRefreshTokenWithInvalidToken() throws ExceptionBase {
        String invalidRefreshToken = "invalidRefreshToken";

        when(jwtManager.validate(invalidRefreshToken)).thenReturn(false);

        assertThrows(InvalidRefreshToken.class, () -> {
            passwordUserService.refreshToken(invalidRefreshToken);
        });
    }

    private Claims createMockClaims(Integer id, boolean rememberMe) {
        Claims mockClaims = Mockito.mock(Claims.class);
        Mockito.when(mockClaims.getSubject()).thenReturn(id.toString());
        Mockito.when(mockClaims.get("rememberMe")).thenReturn(rememberMe);

        return mockClaims;
    }

}