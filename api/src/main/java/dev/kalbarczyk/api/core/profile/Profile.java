package dev.kalbarczyk.api.core.profile;


public record Profile(int userId, String displayName, String avatarUrl, String bio, String location) {
}
