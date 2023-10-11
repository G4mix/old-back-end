package com.gamix.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.records.returns.security.JwtTokens;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class JwtManagerTest {

    @Mock
    private Dotenv dotenv;

    @InjectMocks
    private JwtManager jwtManager;

    @Test
    public void getTokenClaimsValidTokenInput() throws TokenClaimsException {
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("test-secret");

        Integer id = 1;

        JwtTokens jwtTokens = jwtManager.generateJwtTokens(id, "Password123!", false);

        Claims claims = jwtManager.getTokenClaims(jwtTokens.accessToken());

        assertNotNull(claims);
        assertEquals(id, Integer.parseInt(claims.getSubject()));
    }

    @Test
    public void getTokenClaimsMalformedTokenInput() {
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("test-secret");

        String malformedToken = "malformed-token";

        assertThrows(TokenClaimsException.class, () -> jwtManager.getTokenClaims(malformedToken));
    }

    @Test
    public void testGenerateJwtTokens() {
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("your_signing_key_secret");

        JwtTokens jwtTokens = jwtManager.generateJwtTokens(1, "Password123!", true);

        assertNotNull(jwtTokens);
        assertNotNull(jwtTokens.accessToken());
        assertNotNull(jwtTokens.refreshToken());
        assertEquals(jwtTokens.rememberMe(), true);
    }
}
