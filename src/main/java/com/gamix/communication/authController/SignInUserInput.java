package com.gamix.communication.authController;

public record SignInUserInput(String username, String email, String password, boolean rememberMe) {
}
