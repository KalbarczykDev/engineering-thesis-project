package dev.kalbarczyk.api.core.composite.user;

import dev.kalbarczyk.api.core.user.CreateUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

public interface UserCompositeService {

    /**
     * Gets the user aggregate for the given userId.
     *
     * @param userId ID of the user
     * @return the composite user info, if found, else null
     */
    @GetMapping(
            value = "/user-composite/{userId}",
            produces = "application/json")
    UserAggregate getUser(final @PathVariable Long userId);

    /**
     * Creates a new user and minimal profile
     *
     * @param body The user to create
     */
    @PostMapping(
            value = "/user-composite",
            consumes = "application/json"
    )
    void createUser(final CreateUser body);
}
