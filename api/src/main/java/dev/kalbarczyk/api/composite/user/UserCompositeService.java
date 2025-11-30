package dev.kalbarczyk.api.composite.user;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.core.user.CreateUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@SecurityRequirement(name = "security_auth")
@Tag(name = "UserComposite", description = "REST API for composite user information.")
public interface UserCompositeService {

    /**
     * Creates a new user and minimal profile
     *
     * @param body The user to create
     */
    @Operation(
            summary = "${api.user-composite.createUser.description}",
            description = "${api.user-composite.createUser.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PostMapping(
            value = "/user-composite",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<UserProfileComposite> createUser(final @RequestBody CreateUser body);

    /**
     * Gets the user and profile information.
     *
     * @param userId ID of the user
     * @return the composite user info, if found, else null
     */
    @Operation(
            summary = "${api.user-composite.getUserProfile.description}",
            description = "${api.user-composite.getUserProfile.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "${api.responseCodes.ok.description}"),
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "404", description = "${api.responseCodes.notFound.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @GetMapping(
            value = "/user-composite/{userId}/profile",
            produces = "application/json")
    Mono<UserProfileComposite> getUserProfile(final @PathVariable Long userId);

    /**
     * Updates the profile information for the given userId
     *
     * @param userId ID of the user
     * @param body   A JSON representation of the updated profile
     * @return A JSON representation of the updated profile
     */
    @Operation(
            summary = "${api.user-composite.updateProfile.description}",
            description = "${api.user-composite.updateProfile.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @PutMapping(
            value = "/user-composite/{userId}/profile",
            consumes = "application/json",
            produces = "application/json")
    Mono<Profile> updateProfile(final @PathVariable Long userId, final @RequestBody UpdateProfile body);


    /**
     * Deletes a user and associated profile
     *
     * @param userId ID of the user
     */
    @Operation(
            summary = "${api.user-composite.deleteUser.description}",
            description = "${api.user-composite.deleteUser.notes}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "${api.responseCodes.badRequest.description}"),
            @ApiResponse(responseCode = "422", description = "${api.responseCodes.unprocessableEntity.description}")
    })
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping(value = "/user-composite/{userId}")
    Mono<Void> deleteUser(final @PathVariable Long userId);


}
