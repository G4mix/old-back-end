package com.gamix.records.inputs.UserController;

public class PartialUserInput {
    private String username;
    private String icon;

    public PartialUserInput() {}

    public PartialUserInput(String username, String icon) {
        this.username = username;
        this.icon = icon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}