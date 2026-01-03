package dev.kalbarczyk.api.core.workout;

import dev.kalbarczyk.api.core.exercise.Exercise;

public record ExerciseEntry(
        Exercise exercise,
        Series series
) {
}
