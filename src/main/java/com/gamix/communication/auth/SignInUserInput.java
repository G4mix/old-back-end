package com.gamix.communication.auth;

public record SignInUserInput(String username, String email, String password, boolean rememberMe) {
}
