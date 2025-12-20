package dev.kalbarczyk.api.core.exercise;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

public record Exercise(
        Long id,
        @NotBlank @Size(min = 1, max = 255)
        String name,
        @NotBlank @Size(min = 1, max = 255)
        String type,
        @NotBlank @Size(min = 1, max = 255)
        String muscleGroup,
        @Size(max = 2000)
        String instructions,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        String createdAt,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        String updatedAt
) {
}
