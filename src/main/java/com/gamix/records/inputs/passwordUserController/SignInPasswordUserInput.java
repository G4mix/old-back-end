package com.gamix.records.inputs.passwordUserController;

public record SignInPasswordUserInput(String username, String email, String password, boolean rememberMe) {}
