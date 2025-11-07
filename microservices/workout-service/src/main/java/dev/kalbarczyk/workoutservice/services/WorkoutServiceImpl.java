package dev.kalbarczyk.workoutservice.services;

import dev.kalbarczyk.api.core.workout.WorkoutService;
import dev.kalbarczyk.api.core.workout.WorkoutSummary;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class WorkoutServiceImpl implements WorkoutService {
    @Override
    public List<WorkoutSummary> getWorkoutsForUser(final Long userId) {
        return List.of(
                new WorkoutSummary(1L, "Morning Routine", LocalDateTime.now()),
                new WorkoutSummary(2L, "Evening Routine", LocalDateTime.now())
        );
    }
}
