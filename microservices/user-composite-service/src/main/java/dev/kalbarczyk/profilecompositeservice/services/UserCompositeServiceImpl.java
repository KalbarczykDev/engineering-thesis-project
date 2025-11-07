package dev.kalbarczyk.profilecompositeservice.services;

import dev.kalbarczyk.api.core.composite.user.UserProfileComposite;
import dev.kalbarczyk.api.core.composite.user.UserCompositeService;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserCompositeServiceImpl implements UserCompositeService {

    private final UserCompositeIntegration integration;

    @Override
    public UserProfileComposite createUser(final User body) {
        try {
            log.debug("createCompositeUser: creates a new composite entity for username: {}", body.username());
            var result = integration.createUserAndProfile(body);
            var user = result.getFirst();
            var profile = result.getSecond();

            log.debug("createCompositeUser: composite entities created for username: {}", body.username());
            return createUserAggregate(user, profile);
        } catch (InvalidInputException e) {
            log.debug("createCompositeUser failed: {}", e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            log.warn("createCompositeUser failed", e);
            throw e;
        }

    }

    @Override
    public UserProfileComposite getUserProfile(final Long userId) {

        var user = integration.getUser(userId);
        if (user == null) {
            throw new NotFoundException("No user found for userId: " + userId);
        }

        var profile = integration.getProfile(userId);

        if (profile == null) {
            throw new NotFoundException("No profile found for userId: " + userId);
        }

        return createUserAggregate(user, profile);
    }

    @Override
    public Profile updateProfile(final Long userId, final UpdateProfile body) {
        log.debug("updateCompositeUser: updates profile entity for userId: {}", userId);

        log.debug("updateCompositeUser: updated profile entity for userId: {}", userId);
        return null;
    }

    @Override
    public void deleteUser(final Long userId) {
        log.debug("deleteCompositeUser: deletes composite entity for userId: {}", userId);

        integration.deleteUser(userId);
        integration.deleteProfile(userId);

        log.debug("deleteCompositeUser: composite entities deleted for userId: {}", userId);
    }


    private UserProfileComposite createUserAggregate(
            final User user, final Profile profile
    ) {
        return new UserProfileComposite(
                user.userId(),
                user.username(),
                user.slug(),
                user.email(),
                profile.displayName(),
                profile.bio(),
                profile.location(),
                user.createdAt()
        );
    }


}
