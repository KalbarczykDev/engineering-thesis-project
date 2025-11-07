package dev.kalbarczyk.api.core.workout;

import java.time.LocalDateTime;

public record WorkoutSummary(
        String id,
        String name,
        LocalDateTime date
) {
}