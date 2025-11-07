package dev.kalbarczyk.api.core.workout;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface WorkoutService {
    /**
     * Get a list of workout summaries for a specific user.
     *
     * @param userId the ID of the user whose workouts are to be retrieved
     * @return a list of WorkoutSummary objects representing the user's workouts
     */
    @GetMapping(
            value = "/workout/user/{userId}",
            produces = "application/json"
    )
    List<WorkoutSummary> getWorkoutsForUser(final @PathVariable Long userId);

}
