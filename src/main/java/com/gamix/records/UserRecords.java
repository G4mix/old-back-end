package com.gamix.records;

public class UserRecords {
    public record UserInput(String username, String email, String password, String icon, boolean rememberMe) {
        public UserInput(UserInput userInput) {
            this(userInput.username(), userInput.email(), userInput.password(), userInput.icon(), userInput.rememberMe());
        }
    }
    public record PartialUserInput(String username, String icon) {
        public PartialUserInput(UserInput userInput) {
            this(userInput.username(), userInput.icon());
        }
    }
}
