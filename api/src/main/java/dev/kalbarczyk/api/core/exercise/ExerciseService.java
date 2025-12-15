package dev.kalbarczyk.api.core.exercise;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExerciseService {

    /**
     * Creates a new exercise.
     *
     * @param exercise A JSON representation of the new exercise
     * @return A JSON representation of the newly created exercise
     */
    @PutMapping(
            value = "/exercises",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Exercise> createExercise(final @Valid @RequestBody CreateExercise exercise);

    /**
     * Deletes the exercise for the given id.
     *
     * @param id ID of the exercise to delete
     */
    @DeleteMapping(value = "/exercises")
    Mono<Void> deleteExercise(final @RequestParam Long id);

    /**
     * Gets the exercise for the given id.
     *
     * @param id ID of the exercise to get
     * @return the exercise, if found, else null
     */
    @GetMapping(
            value = "/exercises/{id}",
            produces = "application/json"
    )
    Mono<Exercise> getExercise(final @PathVariable Long id);

    /**
     * Updates the exercise for the given id.
     *
     * @param exercise A JSON representation of the updated exercise
     * @return A JSON representation of the updated exercise
     */
    @PutMapping(
            value = "/exercises",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Exercise> updateExercise(@Valid @RequestBody Exercise exercise);


    /**
     * Gets all exercises.
     *
     * @return A list of all exercises
     */
    @GetMapping(
            value = "/exercises",
            produces = "application/json"
    )
    Mono<List<Exercise>> getExercises();


}
