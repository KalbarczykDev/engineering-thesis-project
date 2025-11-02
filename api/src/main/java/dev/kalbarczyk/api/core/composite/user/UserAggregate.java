package dev.kalbarczyk.api.core.composite.user;

public record UserAggregate(
        int userId,
        String username,
        String email,
        String displayName,
        String avatarUrl,
        String bio,
        String location
) {
    //User (username,email)
    //Profile (displayName, avatarUrl, bio, location)
    //TODO: recent workouts list
}
