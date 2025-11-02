package dev.kalbarczyk.api.core.composite.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UserCompositeService {

    /**
     * Sample usage: "curl $HOST:$PORT/user-composite/1".
     *
     * @param userId ID of the product
     * @return the composite product info, if found, else null
     */
    @GetMapping(
            value = "/user-composite/{userId}",
            produces = "application/json")
    UserAggregate getProduct(final @PathVariable int userId);
}
