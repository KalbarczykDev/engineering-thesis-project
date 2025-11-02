package dev.kalbarczyk.api.core.user;

public class User {
    private int userId;
    private String username;
    private String email;
    private String password;
    private String createdAt;
    private String updatedAt;

    public User() {
    }

    public User(int userId, String username, String email, String password, String createdAt, String updatedAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
