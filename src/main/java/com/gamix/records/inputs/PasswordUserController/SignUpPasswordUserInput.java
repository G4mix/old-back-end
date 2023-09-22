package com.gamix.records.inputs.PasswordUserController;

public record SignUpPasswordUserInput(String username, String email, String password) {
    public SignUpPasswordUserInput(SignUpPasswordUserInput signUpPasswordUserInput) {
        this(
            signUpPasswordUserInput.username(), 
            signUpPasswordUserInput.email(), 
            signUpPasswordUserInput.password()
        );
    }
}
