package com.gamix.utils;

import com.gamix.exceptions.ExceptionBase;
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

public class ParameterValidator {
    public static void validateUsername(String username) throws ExceptionBase {
        if (username == null)
            throw new UsernameNull();
        else if (username.isEmpty())
            throw new UsernameEmpty();
        else if (username.length() < 3)
            throw new UsernameTooShort();
        else if (username.length() > 50)
            throw new UsernameTooLong();
        else if (!username.matches("^[a-zA-Z0-9_]*$"))
            throw new UsernameInvalidFormat();
    }

    public static void validatePassword(String password) throws ExceptionBase {
        if (password == null)
            throw new PasswordNull();
        else if (password.length() < 8)
            throw new PasswordTooShort();
        else if (password.length() > 128)
            throw new PasswordTooLong();
        else if (!password.matches(".*\\d.*"))
            throw new PasswordMissingNumber();
        else if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*"))
            throw new PasswordMissingSpecialChar();
        else if (!password.matches(".*[A-Z].*"))
            throw new PasswordMissingUppercase();
    }

    public static void validateEmail(String email) throws ExceptionBase {
        if (email == null)
            throw new EmailNull();
        else if (email.isEmpty())
            throw new EmailEmpty();
        else if (email.length() > 320)
            throw new EmailTooLong();
        else if (!email.matches("^\\w+@gmail\\.com$"))
            throw new EmailInvalidFormat();
    }
}
