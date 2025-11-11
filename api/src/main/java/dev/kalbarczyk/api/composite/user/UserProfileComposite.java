package dev.kalbarczyk.api.composite.user;

public record UserProfileComposite(
        Long userId,
        String username,
        String slug,
        String email,
        String displayName,
        String bio,
        String location,
        String joinedAt
) {
    //User (username,email)
    //Profile (displayName, avatarUrl, bio, location)
    //TODO: recent workouts list
}
