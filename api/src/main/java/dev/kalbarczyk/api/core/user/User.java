package dev.kalbarczyk.api.core.user;

public record User(int userId, String username, String email, String password, String createdAt, String updatedAt) {
}
