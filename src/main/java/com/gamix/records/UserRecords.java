package com.gamix.records;

public class UserRecords {
    public record UserInput(String username, String email, String password, String icon) {
        public UserInput(UserInput userInput) {
            this(userInput.username(), userInput.email(), userInput.password(), userInput.icon());
        }
    }
    public record UserSession(String username, String email, String icon, String accessToken) {
        public UserSession(UserSession userSession) {
            this(userSession.username(), userSession.email(), userSession.icon(), userSession.accessToken());
        }
    }
    public record UserPasswordInput(String password) {
        public UserPasswordInput(UserPasswordInput userInput) {
            this(userInput.password());
        }
    }
    public record PartialUserInput(String username, String icon) {
        public PartialUserInput(UserInput userInput) {
            this(userInput.username(), userInput.icon());
        }
    }
}
