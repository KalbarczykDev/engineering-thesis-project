package dev.kalbarczyk.api.core.profile;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface ProfileService {

    /**
     * Sample usage: "curl $HOST:$PORT/profiles/1".
     *
     * @param userID ID of the user to get the profile for
     * @return the users profile, if found, else null
     */
    @GetMapping(
            value = "/profiles/{userID}",
            produces = "application/json")
    Profile getProfile(@PathVariable int userID);
}
