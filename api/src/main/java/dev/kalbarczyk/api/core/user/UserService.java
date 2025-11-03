package dev.kalbarczyk.api.core.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserService {

    /**
     * Sample usage: "curl $HOST:$PORT/users/1".
     *
     * @param userId ID of the user to get
     * @return the user, if found, else null
     */
    @GetMapping(
            value = "/users/{userId}",
            produces = "application/json"
    )
    User getUser(final @PathVariable int userId);
}
