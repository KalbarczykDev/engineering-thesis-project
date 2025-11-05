package dev.kalbarczyk.api.core.profile;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
     * Deletes the profile for the given userId.
     *
     * @param userId ID of the user to delete the profile for
     */
    @DeleteMapping(value = "/profiles")
    void deleteProfile(final @RequestParam Long userId);


    /**
     * Gets the profile for the given userId.
     *
     * @param userId ID of the user to get the profile for
     * @return the users profile, if found, else null
     */
    @GetMapping(
            value = "/profiles/{userId}",
            produces = "application/json")
    Profile getProfile(final @PathVariable Long userId);


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
    Profile updateProfile(final @PathVariable Long userId, @Valid @RequestBody Profile profile);

    /**
     * Uploads an avatar image for the given userId.
     *
     * @param userId ID of the user to upload the avatar for
     * @param file   The avatar image file
     * @return A JSON representation containing the URL of the uploaded avatar
     */
    @PostMapping(
            value = "/profiles/{userId}/avatar",
            consumes = "multipart/form-data",
            produces = "application/json"
    )
    String uploadAvatar(final @PathVariable Long userId, final @RequestParam MultipartFile file) throws IOException;

    /**
     * Downloads the avatar image for the given userId.
     *
     * @param userId ID of the user to download the avatar for
     * @return The avatar image file
     */
    @GetMapping(
            value = "/profiles/{userId}/avatar",
            produces = "image/jpeg"
    )
    byte[] getAvatar(final @PathVariable Long userId);


}
