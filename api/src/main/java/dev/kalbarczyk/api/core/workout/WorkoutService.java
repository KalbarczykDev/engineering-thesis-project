package dev.kalbarczyk.api.core.workout;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

public interface WorkoutService {


    /**
     * Creates a new workout for user.
     *
     * @param workout A JSON representation of the new workout
     * @return A JSON representation of the newly created workout
     */
    @Operation(
            summary = "${api.workout.createWorkout.description}",
            description = "${api.workout.createWorkout.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
            value = "/workouts",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Workout> createWorkout(final @RequestBody Workout workout);

    /**
     * Updates an existing workout.
     *
     * @param workout A JSON representation of the workout to update
     * @return A JSON representation of the updated workout
     */
    @Operation(
            summary = "${api.workout.updateWorkout.description}",
            description = "${api.workout.updateWorkout.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PutMapping(
            value = "/workouts",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Workout> updateWorkout(final @Valid @RequestBody Workout workout);


    /**
     * Gets the workout history for the given user id.
     *
     * @param userId ID of the user to get workout history for
     * @return the list of workouts for the user
     */
    @Operation(
            summary = "${api.workout.getHistory.description}",
            description = "${api.workout.getHistory.notes}"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
            value = "/workouts/history/{userId}",
            produces = "application/json"
    )
    Mono<List<Workout>> getHistory(final @PathVariable int userId);

}
