package com.gamix.service.UserServiceTest;

import static com.gamix.mock.ClaimsMock.createMockClaims;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.authentication.TokenClaimsException;
import com.gamix.exceptions.user.UserNotFoundByToken;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;
import com.gamix.repositories.UserRepository;
import com.gamix.security.JwtManager;
import com.gamix.service.UserService;
import io.jsonwebtoken.Claims;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtManager jwtManager;

    private Claims mockClaims = createMockClaims(1, true);
    private String validAccessToken = "validAccessToken";

    @Before
    public void setup() throws TokenClaimsException {
        Mockito.lenient().when(jwtManager.getTokenClaims(validAccessToken)).thenReturn(mockClaims);
    }

    @Test
    public void testUpdateUserSuccess() throws ExceptionBase {
        int userId = 1;
        PartialUserInput partialUserInput = new PartialUserInput("newUsername", null);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(partialUserInput.username())).thenReturn(null);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(validAccessToken, partialUserInput);

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));

        assertEquals("newUsername", updatedUser.getUsername());
    }

    @Test
    public void testUpdateUserUserNotFound() throws ExceptionBase {
        int nonExistentUserId = 1;
        PartialUserInput partialUserInput = new PartialUserInput("newUsername", null);

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundByToken.class, () -> {
            userService.updateUser(validAccessToken, partialUserInput);
        });

        verify(userRepository).findById(nonExistentUserId);
    }
}
