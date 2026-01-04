package dev.kalbarczyk.api.core.workout;

import dev.kalbarczyk.api.core.exercise.CreateExercise;

import java.util.List;

public record ExerciseEntry(
        CreateExercise exercise,
        List<Series> series
) {
}
