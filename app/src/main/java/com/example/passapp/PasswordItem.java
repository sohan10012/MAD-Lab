package com.example.passapp;

public class PasswordItem {

    private String app;
    private String username;
    private String password;

    public PasswordItem(String app, String username, String password) {
        this.app = app;
        this.username = username;
        this.password = password;
    }

    // Getters
    public String getApp() {
        return app;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // Setters (useful for editing)
    public void setApp(String app) {
        this.app = app;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
