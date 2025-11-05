package dev.kalbarczyk.api.core.composite.user;

public record UserProfileAggregate(
        Long userId,
        String username,
        String email,
        String displayName,
        String avatarUrl,
        String bio,
        String location,
        String joinedAt
) {
    //User (username,email)
    //Profile (displayName, avatarUrl, bio, location)
    //TODO: recent workouts list
}
