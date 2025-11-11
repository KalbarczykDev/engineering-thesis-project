package dev.kalbarczyk.profilecompositeservice.services;

import dev.kalbarczyk.api.composite.user.UserProfileComposite;
import dev.kalbarczyk.api.composite.user.UserCompositeService;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.core.user.CreateUser;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static java.util.logging.Level.FINE;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserCompositeServiceImpl implements UserCompositeService {

    private final UserCompositeIntegration integration;

    @Override
    public Mono<UserProfileComposite> createUser(final CreateUser body) {
        try {
            log.debug("createCompositeUser: creates a new composite entity for username: {}", body.username());

            var monoResult = integration.createUserAndProfile(body);
            return monoResult.flatMap(tuple -> {
                var user = tuple.getT1();
                var profile = tuple.getT2();
                log.debug("createCompositeUser: composite entities created for username: {}", body.username());
                return createUserAggregate(user, profile);
            });
        } catch (InvalidInputException e) {
            log.debug("createCompositeUser failed: {}", e.getMessage());
            throw e;
        } catch (RuntimeException e) {
            log.warn("createCompositeUser failed", e);
            throw e;
        }

    }

    @Override
    public Mono<UserProfileComposite> getUserProfile(final Long userId) {

        log.info("Will get composite user info for userId: {}", userId);

        return Mono.zip(integration.getUser(userId), integration.getProfile(userId))
                .flatMap(tuple -> createUserAggregate(tuple.getT1(), tuple.getT2()))
                .doOnError(ex -> log.warn("getUserProfile failed: {}", ex.toString()))
                .log(log.getName(), FINE);
    }

    @Override
    public Mono<Profile> updateProfile(final Long userId, final UpdateProfile body) {
        log.debug("updateCompositeUser: updates profile entity for userId: {}", userId);
        var updatedProfile = integration.updateProfile(userId, body);
        log.debug("updateCompositeUser: updated profile entity for userId: {}", userId);
        return updatedProfile;
    }

    @Override
    public Mono<Void> deleteUser(final Long userId) {
        log.debug("deleteCompositeUser: deletes composite entity for userId: {}", userId);

        var userDelete = integration.deleteUser(userId);
        var profileDelete = integration.deleteProfile(userId);

        if (userDelete == null) userDelete = Mono.empty();
        if (profileDelete == null) profileDelete = Mono.empty();

        return Mono.when(userDelete, profileDelete)
                .doOnSuccess(ignored -> log.debug("deleteCompositeUser: composite entities deleted for userId: {}", userId))
                .doOnError(ex -> log.warn("deleteCompositeUser failed: {}", ex.toString()));
    }


    private Mono<UserProfileComposite> createUserAggregate(
            final User user, final Profile profile
    ) {
        return Mono.just(new UserProfileComposite(
                user.userId(),
                user.username(),
                user.slug(),
                user.email(),
                profile.displayName(),
                profile.bio(),
                profile.location(),
                user.createdAt()
        ));
    }


}
