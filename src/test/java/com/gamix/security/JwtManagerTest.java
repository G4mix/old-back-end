package com.gamix.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.records.returns.security.JwtTokens;
import com.gamix.service.InvalidTokenService;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtManagerTest {

    @Mock
    private Dotenv dotenv;

    @Mock
    private InvalidTokenService invalidTokenService;

    @InjectMocks
    private JwtManager jwtManager;

    @Test
    public void getTokenClaims_ValidToken_ReturnsClaims() throws TokenClaimsException {
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("test-secret");

        // Arrange
        JwtTokens jwtTokens = jwtManager.generateJwtTokens("test-user", false);

        // Act
        Claims claims = jwtManager.getTokenClaims(jwtTokens.accessToken());

        // Assert
        assertNotNull(claims);
        assertEquals("test-user", claims.getSubject());
    }

    @Test
    public void getTokenClaims_MalformedToken_ThrowsTokenClaimsException() {
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("test-secret");

        // Arrange
        String malformedToken = "malformed-token";

        // Act & Assert
        assertThrows(TokenClaimsException.class, () -> jwtManager.getTokenClaims(malformedToken));
    }

    @Test
    public void testGenerateJwtTokens() {
        // Configuração dos mocks
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("your_signing_key_secret");

        // Execução do método
        JwtTokens jwtTokens = jwtManager.generateJwtTokens("username", true);

        // Verificação
        assertNotNull(jwtTokens);
        assertNotNull(jwtTokens.accessToken());
        assertNotNull(jwtTokens.refreshToken());
        assertEquals(jwtTokens.rememberMe(), true);
    }
}