package com.gamix.utils;

import com.gamix.enums.ExceptionMessage;
import com.gamix.exceptions.BackendException;

public class ParameterValidator {
    public static void validateUsername(String username) {
        if (username == null) {
            throw new BackendException(ExceptionMessage.USERNAME_NULL);
        }

        if (username.isEmpty()) {
            throw new BackendException(ExceptionMessage.USERNAME_EMPTY);
        }

        if (username.length() < 3) {
            throw new BackendException(ExceptionMessage.USERNAME_TOO_SHORT);
        }

        if (username.length() > 50) {
            throw new BackendException(ExceptionMessage.USERNAME_TOO_LONG);
        }

        if (!username.matches("^[a-zA-Z0-9]*$")) {
            throw new BackendException(ExceptionMessage.USERNAME_INVALID_FORMAT);
        }

        if (username.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new BackendException(ExceptionMessage.USERNAME_CONTAINS_SPECIAL_CHAR);
        }
    }

    public static void validateEmail(String email) {
        if (email == null) {
            throw new BackendException(ExceptionMessage.EMAIL_EMPTY);
        }

        if (email.isEmpty()) {
            throw new BackendException(ExceptionMessage.EMAIL_EMPTY);
        }

        if (email.length() > 320) {
            throw new BackendException(ExceptionMessage.EMAIL_TOO_LONG);
        }

        if (!email.matches("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")) {
            throw new BackendException(ExceptionMessage.EMAIL_INVALID_FORMAT);
        }
    }

    public static void validatePassword(String password) {
        if (password == null) {
            throw new BackendException(ExceptionMessage.PASSWORD_INVALID_FORMAT);
        }

        if (password.length() < 8) {
            throw new BackendException(ExceptionMessage.PASSWORD_TOO_SHORT);
        }

        if (password.length() > 128) {
            throw new BackendException(ExceptionMessage.PASSWORD_TOO_LONG);
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            throw new BackendException(ExceptionMessage.PASSWORD_MISSING_SPECIAL_CHAR);
        }

        if (!password.matches(".*\\d.*")) {
            throw new BackendException(ExceptionMessage.PASSWORD_MISSING_NUMBER);
        }

        if (!password.matches(".*[A-Z].*")) {
            throw new BackendException(ExceptionMessage.PASSWORD_MISSING_UPPERCASE);
        }
    }
}
