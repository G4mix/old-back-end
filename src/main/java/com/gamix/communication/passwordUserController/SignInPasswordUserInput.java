package com.gamix.communication.passwordUserController;

public record SignInPasswordUserInput(String username, String email, String password, boolean rememberMe) {}
