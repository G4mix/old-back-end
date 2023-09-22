package com.gamix.records.inputs.AuthController;

public record SignOutPasswordUserInput(String accessToken, String refreshToken) {
    public SignOutPasswordUserInput(SignOutPasswordUserInput signOutPasswordUserInput) {
        this(signOutPasswordUserInput.accessToken(), signOutPasswordUserInput.refreshToken());
    }
}
