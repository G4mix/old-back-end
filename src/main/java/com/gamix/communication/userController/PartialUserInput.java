package com.gamix.communication.userController;

public record PartialUserInput(String username, String icon) {
    public PartialUserInput(PartialUserInput partialUserInput) {
        this(partialUserInput.username(), partialUserInput.icon());
    }
}
