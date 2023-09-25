package com.gamix.records.inputs.UserController;

public class PartialUserInput {
    private String username;
    private String email;
    private String icon;

    public PartialUserInput() {}

    public PartialUserInput(String username, String email, String icon) {
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}