package dev.kalbarczyk.api.core.exercise;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "${api.exercise.createExercise.description}",
            description = "${api.exercise.createExercise.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
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
    @Operation(
            summary = "${api.exercise.deleteExercise.description}",
            description = "${api.exercise.deleteExercise.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @DeleteMapping(value = "/exercises")
    Mono<Void> deleteExercise(final @RequestParam Long id);

    /**
     * Gets the exercise for the given id.
     *
     * @param id ID of the exercise to get
     * @return the exercise, if found, else null
     */
    @Operation(
            summary = "${api.exercise.getExercise.description}",
            description = "${api.exercise.getExercise.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
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
    @Operation(
            summary = "${api.exercise.updateExercise.description}",
            description = "${api.exercise.updateExercise.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
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
    @Operation(
            summary = "${api.exercise.getExercises.description}",
            description = "${api.exercise.getExercises.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
            value = "/exercises",
            produces = "application/json"
    )
    Mono<List<Exercise>> getExercises();


}
