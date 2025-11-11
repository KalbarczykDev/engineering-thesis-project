package dev.kalbarczyk.api.core.profile;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


public interface ProfileService {

    /**
     * Creates a new user.
     *
     * @param profile A JSON representation of the new profile
     * @return A JSON representation of the newly created profile
     */
    @PostMapping(
            value = "/profiles",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Profile> createProfile(final @Valid @RequestBody Profile profile);

    /**
     * Deletes the profile for the given userId.
     *
     * @param userId ID of the user to delete the profile for
     */
    @DeleteMapping(value = "/profiles")
    Mono<Void> deleteProfile(final @RequestParam Long userId);


    /**
     * Gets the profile for the given userId.
     *
     * @param userId ID of the user to get the profile for
     * @return the users profile, if found, else null
     */
    @GetMapping(
            value = "/profiles/{userId}",
            produces = "application/json")
    Mono<Profile> getProfile(final @PathVariable Long userId);


    /**
     * Updates the profile for the given userId.
     *
     * @param userId  Id of the user to update the profile for
     * @param profile A JSON representation of the updated profile
     * @return A JSON representation of the updated profile
     */
    @PutMapping(
            value = "/profiles/{userId}",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<Profile> updateProfile(final @PathVariable Long userId, @Valid @RequestBody UpdateProfile profile);

}
