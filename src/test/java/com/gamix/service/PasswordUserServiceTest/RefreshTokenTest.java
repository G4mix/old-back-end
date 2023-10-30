package com.gamix.service.PasswordUserServiceTest;

import static com.gamix.mock.ClaimsMock.createMockClaims;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidRefreshToken;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.security.JwtManager;
import com.gamix.service.PasswordUserService;
import com.gamix.service.UserService;
import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class RefreshTokenTest {

    @Mock
    private JwtManager jwtManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordUserService passwordUserService;

    @Test
    public void refreshTokenWithValidToken() throws ExceptionBase {
        String validRefreshToken = "validRefreshToken";
        when(userService.findUserById(any(Integer.class))).thenReturn(
                new User().setId(1).setPasswordUser(new PasswordUser().setPassword("password123")));
        when(jwtManager.validate(validRefreshToken)).thenReturn(true);

        Claims mockClaims = createMockClaims(1, true);
        when(jwtManager.getTokenClaims(validRefreshToken)).thenReturn(mockClaims);

        when(jwtManager.generateJwtTokens(eq(1), any(String.class), eq(true)))
                .thenReturn(new JwtTokens("newAccessToken", "newRefreshToken", true));

        JwtTokens tokens = passwordUserService.refreshToken(validRefreshToken);

        assertEquals("newAccessToken", tokens.accessToken());
        assertEquals("newRefreshToken", tokens.refreshToken());
    }

    @Test
    public void refreshTokenWithInvalidToken() throws ExceptionBase {
        String invalidRefreshToken = "invalidRefreshToken";

        when(jwtManager.validate(invalidRefreshToken)).thenReturn(false);

        assertThrows(InvalidRefreshToken.class, () -> {
            passwordUserService.refreshToken(invalidRefreshToken);
        });
    }

}
