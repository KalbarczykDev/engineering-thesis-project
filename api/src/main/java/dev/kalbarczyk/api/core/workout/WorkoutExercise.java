package dev.kalbarczyk.api.core.workout;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WorkoutExercise(
        @Valid @NotNull Exercise exercise,
        @Valid @NotNull List<Series> series
) {
}