package dev.kalbarczyk.api.core.exercise;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Exercise(
        Long id,
        @NotBlank @Size(min = 1, max = 255)
        String name,
        @NotBlank @Size(min = 1, max = 255)
        String type,
         @NotBlank @Size(min = 1, max = 255)
        String muscleGroup,
        @Size(min = 1, max = 2000)
        String instructions
) {
}
