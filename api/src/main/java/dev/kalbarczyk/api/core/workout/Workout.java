package dev.kalbarczyk.api.core.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record Workout(
        String id,
        String name,
        @NotNull Long userId,
        @NotNull LocalDateTime date,
        @Valid List<WorkoutExercise> exercises
) {
}