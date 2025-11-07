package dev.kalbarczyk.api.core.composite.workout;

import dev.kalbarczyk.api.core.exercise.Exercise;
import dev.kalbarczyk.api.core.workout.Workout;
import dev.kalbarczyk.api.core.workout.WorkoutSummary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface WorkoutCompositeService {

    /**
     * Gets the list of workouts summaries for the given userId
     *
     * @param userId ID of the user
     * @return the list of workouts summaries, if any, else empty list
     */
    @GetMapping(
            value = "/workout-composite/users/{userId}",
            produces = "application/json"
    )
    List<WorkoutSummary> getWorkoutsForUser(final @PathVariable Long userId);

    /**
     * Adds a new workout to the given userId
     *
     * @param userId  ID of the user
     * @param workout A JSON representation of the workout to add
     * @return A JSON representation of the added workout summary
     */
    @PostMapping(
            value = "/workout-composite/users/{userId}",
            consumes = "application/json",
            produces = "application/json"
    )
    WorkoutSummary addWorkoutToUser(final @PathVariable Long userId, @RequestBody final Workout workout);

    /**
     * Updates an existing workout for the given userId
     *
     * @param userId  ID of the user
     * @param workout A JSON representation of the workout to update
     * @return A JSON representation of the updated workout summary
     */
    @PostMapping(
            value = "/workout-composite/users/{userId}/update",
            consumes = "application/json",
            produces = "application/json"
    )
    WorkoutSummary updateWorkoutForUser(final @PathVariable Long userId, @RequestBody final Workout workout);

    /**
     * Gets the list of available exercises
     *
     * @return the list of available exercises
     */
    @GetMapping(
            value = "/workout-composite/exercises",
            produces = "application/json"
    )
    List<Exercise> getAvailableExercises();


}
