package com.gamix.service.UserServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.User;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class CreateUserTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateUserSuccess() throws ExceptionBase {
        // Arrange
        String username = "testuser";
        String email = "test@example.com";
        String icon = "default.png";
        User user = new User().setUsername(username).setEmail(email).setIcon(icon);

        // Configurando o mock para retornar o usuário criado
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User createdUser = userService.createUser(username, email, icon);

        // Assert
        assertEquals(user, createdUser);
    }

}
