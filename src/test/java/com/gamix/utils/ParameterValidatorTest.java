package com.gamix.utils;

import com.gamix.exceptions.parameters.password.PasswordInvalidFormat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class ParameterValidatorTest {

    @Test
    public void validatePassword() {
        assertThrows(PasswordInvalidFormat.class, () -> ParameterValidator.validatePassword(null));
        assertThrows(PasswordInvalidFormat.class, () -> ParameterValidator.validatePassword("1234567"));
        assertThrows(PasswordInvalidFormat.class, () -> ParameterValidator.validatePassword("a".repeat(129)));
        assertThrows(PasswordInvalidFormat.class, () -> ParameterValidator.validatePassword("Password"));
        assertThrows(PasswordInvalidFormat.class, () -> ParameterValidator.validatePassword("password1"));
        assertThrows(PasswordInvalidFormat.class, () -> ParameterValidator.validatePassword("password1!"));
    }
}
