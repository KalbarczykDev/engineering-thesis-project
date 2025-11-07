package dev.kalbarczyk.api.core.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfile(
        @NotBlank @Size(min = 1, max = 100)
        String displayName,
        @Size(max = 2000)
        String bio,
        String location) {
}