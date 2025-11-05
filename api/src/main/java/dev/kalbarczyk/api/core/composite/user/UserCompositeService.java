package dev.kalbarczyk.api.core.composite.user;

import dev.kalbarczyk.api.core.user.User;
import org.springframework.web.bind.annotation.*;

public interface UserCompositeService {

    /**
     * Creates a new user and minimal profile
     *
     * @param body The user to create
     */
    @PostMapping(
            value = "/user-composite",
            consumes = "application/json"
    )
    void createUser(final @RequestBody User body);

    /**
     * Gets the user and profile information.
     *
     * @param userId ID of the user
     * @return the composite user info, if found, else null
     */
    @GetMapping(
            value = "/user-composite/{userId}/profile",
            produces = "application/json")
    UserProfileAggregate getUserProfile(final @PathVariable Long userId);


    /**
     * Deletes a user and associated profile
     *
     * @param userId ID of the user
     */
    @DeleteMapping(value = "/user-composite/{userId}")
    void deleteUser(final @PathVariable Long userId);


}
