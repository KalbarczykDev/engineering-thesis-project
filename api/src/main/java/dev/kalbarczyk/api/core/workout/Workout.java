package dev.kalbarczyk.api.core.workout;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public record Workout(
        String id,
        Long userId,

        @NotBlank @NotEmpty @Size(max = 1000)
        String name,

        List<ExerciseEntry> exercises,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        String createdAt,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        String updatedAt
) {
}
