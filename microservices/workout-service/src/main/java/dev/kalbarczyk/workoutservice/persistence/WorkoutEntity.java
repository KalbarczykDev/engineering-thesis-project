package dev.kalbarczyk.workoutservice.persistence;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "workouts")
@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutEntity {
    @Id
    private String id;
    private Long userId;
    private String name;
    private LocalDateTime date;
    private List<WorkoutExercise> exercises;


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WorkoutExercise {
        private Exercise exercise;
        private List<Series> series;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Exercise {
        private Long id;
        private String name;
        private String type;
        private String muscleGroup;
        private String instructions;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Series {
        private int reps;
        private double weight;
        private double rest;
    }
}
