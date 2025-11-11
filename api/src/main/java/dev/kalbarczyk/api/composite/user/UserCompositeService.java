package dev.kalbarczyk.api.composite.user;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.core.user.CreateUser;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public interface UserCompositeService {

    /**
     * Creates a new user and minimal profile
     *
     * @param body The user to create
     */
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
    @DeleteMapping(value = "/user-composite/{userId}")
    Mono<Void> deleteUser(final @PathVariable Long userId);


}
