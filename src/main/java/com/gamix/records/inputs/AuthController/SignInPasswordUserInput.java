package com.gamix.records.inputs.AuthController;

public record SignInPasswordUserInput(
    String username, String email, 
    String password, boolean rememberMe
) {
    public SignInPasswordUserInput(SignInPasswordUserInput signInPasswordUserInput) {
        this(
            signInPasswordUserInput.username(), 
            signInPasswordUserInput.email(), 
            signInPasswordUserInput.password(), 
            signInPasswordUserInput.rememberMe()
        );
    }
}