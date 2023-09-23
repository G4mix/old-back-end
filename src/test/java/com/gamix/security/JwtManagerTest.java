package com.gamix.security;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.gamix.exceptions.authentication.*;
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

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("secretKey");
    }

    @Test
    public void testGetTokenClaims() throws TokenClaimsException {
        String token = "validToken";

        Claims claims = jwtManager.getTokenClaims(token);

        // Verifique se o claims está correto
        // ...

        // Verifique se o método de parseClaimsJws foi chamado com o token correto
        // ...
    }

    @Test(expected = TokenClaimsException.class)
    public void testGetTokenClaims_InvalidToken() throws TokenClaimsException {
        String invalidToken = "invalidToken";

        jwtManager.getTokenClaims(invalidToken);
    }

    @Test
    public void testValidate_ValidToken() throws TokenValidationException {
        String validToken = "validToken";

        boolean isValid = jwtManager.validate(validToken);

        assertTrue(isValid);
        // Verifique se o método isTokenOnBlacklist foi chamado
        // ...
    }

    @Test
    public void testValidate_ExpiredToken() throws TokenValidationException {
        String expiredToken = "expiredToken";

        boolean isValid = jwtManager.validate(expiredToken);

        assertFalse(isValid);
    }

    @Test
    public void testValidate_TokenOnBlacklist() throws TokenValidationException {
        String blacklistedToken = "blacklistedToken";
        when(invalidTokenService.isTokenOnBlacklist(blacklistedToken)).thenReturn(true);

        boolean isValid = jwtManager.validate(blacklistedToken);

        assertFalse(isValid);
    }

    @Test(expected = TokenValidationException.class)
    public void testValidate_InvalidToken() throws TokenValidationException, TokenClaimsException {
        String invalidToken = "invalidToken";
        when(dotenv.get("JWT_SIGNING_KEY_SECRET")).thenReturn("invalidSecretKey");

        jwtManager.validate(invalidToken);
    }

    @Test
    public void testInvalidate() throws TokenInvalidationException, TokenClaimsException {
        String tokenToInvalidate = "tokenToInvalidate";
        Claims claims = mock(Claims.class);
        when(claims.getExpiration()).thenReturn(new Date());
        when(jwtManager.getTokenClaims(tokenToInvalidate)).thenReturn(claims);

        jwtManager.invalidate(tokenToInvalidate);

        // Verifique se o método addInvalidToken foi chamado com os argumentos corretos
        // ...
    }

    @Test(expected = TokenInvalidationException.class)
    public void testInvalidate_InvalidToken() throws TokenInvalidationException, TokenClaimsException {
        String invalidToken = "invalidToken";

        jwtManager.invalidate(invalidToken);
    }

    @Test
    public void testGenerateJwtTokens() throws JwtTokensGenerationException {
        String username = "testUser";
        boolean rememberMe = true;

        JwtTokens jwtTokens = jwtManager.generateJwtTokens(username, rememberMe);

        // Verifique se jwtTokens foi gerado corretamente
        // ...
    }
}
