package dev.kalbarczyk.profileservice.services;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.ProfileService;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.profileservice.persistence.ProfileRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static java.util.logging.Level.FINE;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    private final ProfileMapper mapper;


    @Override
    public Mono<Profile> createProfile(final @Valid Profile profile) {

        if (profile.userId() < 1) {
            throw new InvalidInputException("Invalid userId:" + profile.userId());
        }

        log.info("createProfile: tries to create profile for userId {}", profile.userId());

        return repository.save(mapper.apiToEntity(profile))
                .log(log.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        _ -> new InvalidInputException("Duplicate key,  userId: " + profile.userId()))
                .map(mapper::entityToApi);
    }

    @Override
    public Mono<Void> deleteProfile(final Long userId) {

        if (userId < 1) {
            throw new InvalidInputException("Invalid userId:" + userId);
        }

        log.debug("deleteProfile: tries to delete an entity with userId: {}", userId);
        return repository.findByUserId(userId).log(log.getName(), FINE).map(repository::delete).flatMap(e -> e);
    }

    @Override
    public Mono<Profile> getProfile(final Long userId) {
        log.debug("getProfile: tries to get an entity with userId: {}", userId);

        if (userId < 1) {
            throw new InvalidInputException("Invalid userId:" + userId);
        }

        log.info("Will get profile info for userId={}", userId);

        return repository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("No profile found for userId: " + userId)))
                .log(log.getName(), FINE)
                .map(mapper::entityToApi);

    }

    @Override
    public Mono<Profile> updateProfile(final Long userId, final UpdateProfile profile) {
        log.debug("updateProfile: tries to get an entity with userId: {}", userId);

        if (userId < 1) {
            throw new InvalidInputException("Invalid userId:" + userId);
        }

        return repository.findByUserId(userId)
                .switchIfEmpty(Mono.error(new NotFoundException("No profile found for userId: " + userId)))
                .flatMap(existingEntity -> {
                    existingEntity.setDisplayName(profile.displayName());
                    existingEntity.setBio(profile.bio());
                    existingEntity.setLocation(profile.location());
                    return repository.save(existingEntity);
                })
                .map(mapper::entityToApi)
                .doOnSuccess(updated ->
                        log.debug("updateProfile: modified an entity with userId: {}", updated.userId())
                )
                .log(log.getName(), FINE);
    }
}
