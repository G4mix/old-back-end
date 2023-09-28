package com.gamix.service.UserServiceTest;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.user.UserNotFoundByToken;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.service.UserService;

import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class FindUserByToken {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtManager jwtManager;

    private Claims createMockClaims(String subject) {
        Claims claims = mock(Claims.class);
        when(claims.getSubject()).thenReturn(subject);
        return claims;
    }

    @Test
    public void testFindUserByToken() throws ExceptionBase {
        String accessToken = "mockedAccessToken";
        String username = "testuser";
        Claims mockClaims = createMockClaims(username);

        when(jwtManager.getTokenClaims(accessToken)).thenReturn(mockClaims);

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByToken(accessToken);

        assertEquals(user, foundUser);
    }

    @Test
    public void testFindUserByTokenNotFound() throws ExceptionBase {
        String accessToken = "mockedAccessToken";
        String username = "testuser";
        Claims mockClaims = createMockClaims(username);

        when(jwtManager.getTokenClaims(accessToken)).thenReturn(mockClaims);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundByToken.class, () -> {
            userService.findUserByToken(accessToken);
        });
    }
}
