package dev.kalbarczyk.api.core.workout;

public record Series(
        int reps,
        double weight,
        int restTimeSeconds
) {
}
