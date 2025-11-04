package dev.kalbarczyk.api.core.profile;


public record Profile(Long userId, String displayName, String avatarUrl, String bio, String location,String createdAt,String updatedAt) {
}
