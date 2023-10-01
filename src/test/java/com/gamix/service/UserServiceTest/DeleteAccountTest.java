package com.gamix.service.UserServiceTest;

import static com.gamix.mock.ClaimsMock.createMockClaims;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.InvalidAccessToken;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.service.UserService;

import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class DeleteAccountTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtManager jwtManager;

    @Test
    public void testDeleteAccountSuccess() throws ExceptionBase {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        
        String validAccessToken = "validAccessToken";
        when(jwtManager.validate(validAccessToken)).thenReturn(true);
        Claims mockClaims = createMockClaims(userId, true);
        when(jwtManager.getTokenClaims(validAccessToken)).thenReturn(mockClaims);

        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        userService.deleteAccount(validAccessToken);

        verify(userRepository).existsById(userId);
        verify(userRepository).deleteById(userId);
    }

    @Test
    public void testDeleteAccountInvalidAccessTokenException() throws ExceptionBase {        
        String validAccessToken = "validAccessToken";
        when(jwtManager.validate(validAccessToken)).thenReturn(false);

        assertThrows(InvalidAccessToken.class, () -> {
            userService.deleteAccount(validAccessToken);
        });
    }
}
