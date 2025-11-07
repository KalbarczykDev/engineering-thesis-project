package dev.kalbarczyk.api.core.workout;

import java.time.LocalDateTime;

public record WorkoutSummary(
        Long id,
        String name,
        LocalDateTime date
) {
}