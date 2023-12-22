package com.gamix.communication.userController;

public record SignInUserInput(String username, String email, String password, boolean rememberMe) {
}
