package dev.kalbarczyk.api.core.workout;

import dev.kalbarczyk.api.core.exercise.Exercise;

import java.util.List;

public record ExerciseEntry(
        Exercise exercise,
        List<Series> series
) {
}
