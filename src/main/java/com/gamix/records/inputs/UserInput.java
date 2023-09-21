package com.gamix.records.inputs;

public record UserInput(String username, String email, String password, String icon, boolean rememberMe) {
    public UserInput(UserInput userInput) {
        this(userInput.username(), userInput.email(), userInput.password(), userInput.icon(), userInput.rememberMe());
    }
}