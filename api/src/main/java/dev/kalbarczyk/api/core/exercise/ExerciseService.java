package dev.kalbarczyk.api.core.exercise;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

public interface ExerciseService {
    /**
     * Creates a new exercise.
     *
     * @param exercise A JSON representation of the new exercise
     * @return A JSON representation of the newly created exercise
     */
    @PostMapping(
            value = "/exercises",
            consumes = "application/json",
            produces = "application/json"
    )
    Exercise createExercise(final @Valid Exercise exercise);

    /**
     * Deletes the exercise for the given exerciseId.
     *
     * @param exerciseId ID of the exercise to delete
     */
    @DeleteMapping(value = "/exercises")
    void delteExercise(final @RequestParam Long exerciseId);

    /**
     * Gets the exercise for the given exerciseId.
     *
     * @param exerciseId ID of the exercise to get
     * @return the exercise, if found, else null
     */
    @GetMapping(
            value = "/exercises/{exerciseId}",
            produces = "application/json"
    )
    Exercise getExercise(final @PathVariable Long exerciseId);

    /**
     * Updates the exercise for the given exerciseId.
     *
     * @param exerciseId Id of the exercise to update
     * @param exercise   A JSON representation of the updated exercise
     * @return A JSON representation of the updated exercise
     */
    @PutMapping(
            value = "/exercises/{exerciseId}",
            consumes = "application/json",
            produces = "application/json"
    )
    Exercise updateExercise(final @PathVariable Long exerciseId, @Valid @RequestBody Exercise exercise);


}
