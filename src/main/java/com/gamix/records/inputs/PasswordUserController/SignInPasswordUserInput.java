package com.gamix.records.inputs.PasswordUserController;

public record SignInPasswordUserInput(String username, String email, String password,
        boolean rememberMe) {
}
