package dev.kalbarczyk.api.core.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateWorkout(
        @NotBlank @Size(min = 1, max = 255)
        String name,
        @NotBlank @Size(min = 1, max = 255)
        String type,
        @NotBlank @Size(min = 1, max = 255)
        String muscleGroup,
        @Size(max = 2000)
        String instructions
) {
}
