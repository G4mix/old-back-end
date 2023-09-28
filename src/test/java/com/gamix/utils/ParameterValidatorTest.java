package com.gamix.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.gamix.exceptions.parameters.email.EmailEmpty;
import com.gamix.exceptions.parameters.email.EmailInvalidFormat;
import com.gamix.exceptions.parameters.email.EmailNull;
import com.gamix.exceptions.parameters.email.EmailTooLong;
import com.gamix.exceptions.parameters.password.PasswordMissingNumber;
import com.gamix.exceptions.parameters.password.PasswordMissingSpecialChar;
import com.gamix.exceptions.parameters.password.PasswordMissingUppercase;
import com.gamix.exceptions.parameters.password.PasswordNull;
import com.gamix.exceptions.parameters.password.PasswordTooLong;
import com.gamix.exceptions.parameters.password.PasswordTooShort;
import com.gamix.exceptions.parameters.username.UsernameEmpty;
import com.gamix.exceptions.parameters.username.UsernameInvalidFormat;
import com.gamix.exceptions.parameters.username.UsernameNull;
import com.gamix.exceptions.parameters.username.UsernameTooLong;
import com.gamix.exceptions.parameters.username.UsernameTooShort;

@RunWith(MockitoJUnitRunner.class)
public class ParameterValidatorTest {
    
    @Test
    public void validateUsername() {
        assertThrows(UsernameNull.class, () -> ParameterValidator.validateUsername(null));
        assertThrows(UsernameEmpty.class, () -> ParameterValidator.validateUsername(""));
        assertThrows(UsernameTooShort.class, () -> ParameterValidator.validateUsername("ab"));
        assertThrows(UsernameTooLong.class, () -> ParameterValidator.validateUsername("123456789012345678901234567890123456789012345678901"));
        assertThrows(UsernameInvalidFormat.class, () -> ParameterValidator.validateUsername("user@name"));
    }

    @Test
    public void validateEmail() {
        assertThrows(EmailNull.class, () -> ParameterValidator.validateEmail(null));
        assertThrows(EmailEmpty.class, () -> ParameterValidator.validateEmail(""));
        assertThrows(EmailTooLong.class, () -> ParameterValidator.validateEmail("a".repeat(330)+"@gmail.com"));
        assertThrows(EmailInvalidFormat.class, () -> ParameterValidator.validateEmail("invalidemail"));
    }

    @Test
    public void validatePassword() {
        assertThrows(PasswordNull.class, () -> ParameterValidator.validatePassword(null));
        assertThrows(PasswordTooShort.class, () -> ParameterValidator.validatePassword("1234567"));
        assertThrows(PasswordTooLong.class, () -> ParameterValidator.validatePassword("a".repeat(129)));
        assertThrows(PasswordMissingSpecialChar.class, () -> ParameterValidator.validatePassword("password1"));
        assertThrows(PasswordMissingNumber.class, () -> ParameterValidator.validatePassword("Password"));
        assertThrows(PasswordMissingUppercase.class, () -> ParameterValidator.validatePassword("password1!"));
    }
}
