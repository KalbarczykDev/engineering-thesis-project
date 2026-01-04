package dev.kalbarczyk.api.core.workout;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CreateWorkout(
        Long userId,
        @NotBlank @NotEmpty @Size(max = 1000)
        String name,
        List<ExerciseEntry> exercises) {
}
