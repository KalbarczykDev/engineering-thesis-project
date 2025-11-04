package dev.kalbarczyk.api.core.profile;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    Profile createProfile(final @Valid @RequestBody Profile profile);

    /**
     * Deletes the profile for the given userID.
     *
     * @param userID ID of the user to delete the profile for
     */
    @DeleteMapping(value = "/profiles")
    void deleteProfile(final @RequestParam Long userID);


    /**
     * Gets the profile for the given userID.
     *
     * @param userID ID of the user to get the profile for
     * @return the users profile, if found, else null
     */
    @GetMapping(
            value = "/profiles/{userID}",
            produces = "application/json")
    Profile getProfile(final @PathVariable int userID);


    /**
     * Updates the profile for the given userID.
     *
     * @param userID ID of the user to update the profile for
     * @param profile   A JSON representation of the updated profile
     * @return A JSON representation of the updated profile
     */
    @PutMapping(
            value = "/profiles/{userID}",
            consumes = "application/json",
            produces = "application/json"
    )
    Profile updateProfile(final @PathVariable int userID, @Valid @RequestBody Profile profile);


}
