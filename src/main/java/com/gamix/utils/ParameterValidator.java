package com.gamix.utils;

import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.parameters.EmailInvalidFormat;
import com.gamix.exceptions.parameters.UsernameInvalidFormat;
import com.gamix.exceptions.parameters.password.PasswordInvalidFormat;

import java.util.regex.Pattern;

public class ParameterValidator {
    public static void validateUsername(String username) throws ExceptionBase {
        if (username == null) throw new UsernameInvalidFormat();
        String usernameRegex = "^[a-zA-Z0-9_]{3,50}$";
        boolean usernameIsValid = Pattern.matches(usernameRegex, username);
        if (!usernameIsValid) throw new UsernameInvalidFormat();
    }

    public static void validateEmail(String email) throws ExceptionBase {
        if (email == null) throw new EmailInvalidFormat();
        String emailRegex = "^(?=.{10,320}$)\\w+@gmail\\.com$";
        boolean emailIsValid = Pattern.matches(emailRegex, email);
        if (!emailIsValid) throw new EmailInvalidFormat();
    }

    public static void validatePassword(String password) throws ExceptionBase {
        if (password == null) throw new PasswordInvalidFormat();
        String passwordRegex = "^(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,128}$";
        boolean passwordIsValid = Pattern.matches(passwordRegex, password);
        if (!passwordIsValid) throw new PasswordInvalidFormat();
    }
}
