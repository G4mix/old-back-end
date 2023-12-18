package com.gamix.records.passwordUserController;

public record SignInPasswordUserInput(String username, String email, String password, boolean rememberMe) {}
