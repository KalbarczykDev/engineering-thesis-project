package dev.kalbarczyk.api.core.user;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

public interface UserService {

    /**
     * Creates a new user.
     *
     * @param body A JSON representation of the new user
     * @return A JSON representation of the newly created user
     */
    @PostMapping(
            value = "/users",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<User> createUser(final @Valid @RequestBody CreateUser body);

    /**
     * Deletes the user with the given userId.
     *
     * @param userId ID of the user to delete
     */
    @DeleteMapping(value = "/users")
    Mono<Void> deleteUser(final @RequestParam Long userId);

    /**
     * Gets the user with the given userId.
     *
     * @param userId ID of the user to get
     * @return the user, if found, else null
     */
    @GetMapping(
            value = "/users/{userId}",
            produces = "application/json"
    )
    Mono<User> getUser(final @PathVariable Long userId);


    /**
     * Updates the user with the given userId.
     *
     * @param userId ID of the user to update
     * @param body   A JSON representation of the updated user
     * @return A JSON representation of the updated user
     */
    @PutMapping(
            value = "/users/{userId}",
            consumes = "application/json",
            produces = "application/json"
    )
    Mono<User> updateUser(final @PathVariable Long userId, final @Valid @RequestBody User body);
}
