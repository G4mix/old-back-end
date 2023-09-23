package com.gamix.utils;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.parameters.email.*;
import com.gamix.exceptions.parameters.password.*;
import com.gamix.exceptions.parameters.username.*;

public class ParameterValidator {
    public static void validateUsername(String username) throws ExceptionBase {
        if (username == null) throw new UsernameNull();
        if (username.isEmpty()) throw new UsernameEmpty();
        if (username.length() < 3) throw new UsernameTooShort();
        if (username.length() > 50) throw new UsernameTooLong();
        if (!username.matches("^[a-zA-Z0-9]*$")) throw new UsernameInvalidFormat();
        if (username.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) throw new UsernameContainsSpecialChar();
    }

    public static void validateEmail(String email) throws ExceptionBase {
        if (email == null) throw new EmailNull();
        if (email.isEmpty()) throw new EmailEmpty();
        if (email.length() > 320) throw new EmailTooLong();
        if (!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) throw new EmailInvalidFormat();
    }

    public static void validatePassword(String password) throws ExceptionBase {
        if (password == null) throw new PasswordNull();
        if (password.length() < 8) throw new PasswordTooShort();
        if (password.length() > 128) throw new PasswordTooLong();
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) throw new PasswordMissingSpecialChar();
        if (!password.matches(".*\\d.*")) throw new PasswordMissingUppercase();
        if (!password.matches(".*[A-Z].*")) throw new PasswordMissingUppercase();
    }
}
