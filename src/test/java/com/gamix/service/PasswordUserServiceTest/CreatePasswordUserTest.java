package com.gamix.service.PasswordUserServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.models.PasswordUser;
import com.gamix.models.User;
import com.gamix.repositories.PasswordUserRepository;
import com.gamix.service.PasswordUserService;

@RunWith(MockitoJUnitRunner.class)
public class CreatePasswordUserTest {

    @InjectMocks
    private PasswordUserService passwordUserService;

    @Mock
    private PasswordUserRepository passwordUserRepository;

    @Test
    public void testCreatePasswordUser() throws ExceptionBase {
        String username = "testuser";
        String email = "test@example.com";
        String icon = "default.png";
        User user = new User().setUsername(username).setEmail(email).setIcon(icon);

        String password = "testpassword";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);

        PasswordUser passwordUser = new PasswordUser()
            .setUser(user)
            .setPassword(encodedPassword)
            .setVerifiedEmail(false);

        when(passwordUserRepository.save(any(PasswordUser.class))).thenReturn(passwordUser);

        PasswordUser createdPasswordUser = passwordUserService.createPasswordUser(user, password);

        assertEquals(passwordUser, createdPasswordUser);
    }
}
