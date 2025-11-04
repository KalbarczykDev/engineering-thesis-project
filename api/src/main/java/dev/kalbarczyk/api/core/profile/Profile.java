package dev.kalbarczyk.api.core.profile;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Profile(
        Long userId,
        @NotBlank @Size(min = 1, max = 100)
        String displayName,
        String avatarUrl,
        @Size(max = 2000)
        String bio,
        String location,
        String createdAt,
        String updatedAt) {
}
