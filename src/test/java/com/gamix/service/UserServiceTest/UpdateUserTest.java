package com.gamix.service.UserServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.user.UserNotFoundById;
import com.gamix.models.User;
import com.gamix.records.inputs.UserController.PartialUserInput;
import com.gamix.repositories.UserRepository;
import com.gamix.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class UpdateUserTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testUpdateUserSuccess() throws ExceptionBase {
        int userId = 1;
        PartialUserInput partialUserInput = new PartialUserInput();
        partialUserInput.setUsername("newUsername");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(userId, partialUserInput);

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));

        assertEquals("newUsername", updatedUser.getUsername());
    }

    @Test
    public void testUpdateUserUserNotFound() throws ExceptionBase {
        int nonExistentUserId = 2;
        PartialUserInput partialUserInput = new PartialUserInput();
        partialUserInput.setUsername("newUsername");

        when(userRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundById.class, () -> {
            userService.updateUser(nonExistentUserId, partialUserInput);
        });

        verify(userRepository).findById(nonExistentUserId);
    }

    @Test
    public void testUpdateUserExceptionThrown() throws ExceptionBase {
        int userId = 3;
        PartialUserInput partialUserInput = new PartialUserInput();
        partialUserInput.setUsername("newUsername");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("oldUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Something went wrong"));

        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userId, partialUserInput);
        });

        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }
}
