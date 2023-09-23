package com.gamix.utils;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.parameters.username.UsernameContainsSpecialChar;
import com.gamix.exceptions.parameters.username.UsernameEmpty;
import com.gamix.exceptions.parameters.username.UsernameInvalidFormat;
import com.gamix.exceptions.parameters.username.UsernameNull;
import com.gamix.exceptions.parameters.username.UsernameTooLong;
import com.gamix.exceptions.parameters.username.UsernameTooShort;

/**
 * 
 * @Throws UsernameNull
 */
public class ParameterValidator {
    public static void validateUsername(String username) 
        throws UsernameNull, UsernameEmpty, UsernameTooShort, 
        UsernameTooLong, UsernameInvalidFormat, UsernameContainsSpecialChar 
    {
        if (username == null) throw new UsernameNull();
        if (username.isEmpty()) throw new UsernameEmpty();
        if (username.length() < 3) throw new UsernameTooShort();
        if (username.length() > 50) throw new UsernameTooLong();
        if (!username.matches("^[a-zA-Z0-9]*$")) throw new UsernameInvalidFormat();
        if (username.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) throw new UsernameContainsSpecialChar();
    }

    public static void validateEmail(String email) throws ExceptionBase {
        if (email == null) {
            throw new ExceptionBase(ExceptionMessage.EMAIL_EMPTY);
        }

        if (email.isEmpty()) {
            throw new ExceptionBase(ExceptionMessage.EMAIL_EMPTY);
        }

        if (email.length() > 320) {
            throw new ExceptionBase(ExceptionMessage.EMAIL_TOO_LONG);
        }

        if (!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
            throw new ExceptionBase(ExceptionMessage.EMAIL_INVALID_FORMAT);
        }
    }

    public static void validatePassword(String password) throws ExceptionBase {
        if (password == null) {
            throw new ExceptionBase(ExceptionMessage.PASSWORD_INVALID_FORMAT);
        }

        if (password.length() < 8) {
            throw new ExceptionBase(ExceptionMessage.PASSWORD_TOO_SHORT);
        }

        if (password.length() > 128) {
            throw new ExceptionBase(ExceptionMessage.PASSWORD_TOO_LONG);
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new ExceptionBase(ExceptionMessage.PASSWORD_MISSING_SPECIAL_CHAR);
        }

        if (!password.matches(".*\\d.*")) {
            throw new ExceptionBase(ExceptionMessage.PASSWORD_MISSING_NUMBER);
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new ExceptionBase(ExceptionMessage.PASSWORD_MISSING_UPPERCASE);
        }
    }
}
