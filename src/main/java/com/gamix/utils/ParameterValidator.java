package com.gamix.utils;

import java.util.regex.Pattern;
import com.gamix.exceptions.ExceptionBase;
import com.gamix.exceptions.parameters.password.PasswordInvalidFormat;

public class ParameterValidator {
    public static void validatePassword(String password) throws ExceptionBase {
        String passwordRegex = "^(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[A-Z]).{8,128}$";
        boolean passwordIsValid = Pattern.matches(passwordRegex, password);
        if (!passwordIsValid) throw new PasswordInvalidFormat();
    }
}
