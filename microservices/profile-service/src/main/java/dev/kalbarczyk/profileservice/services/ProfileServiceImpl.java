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
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository repository;
    private final ProfileMapper mapper;
    private final GridFSBucket bucket;


    @Override
    public Profile createProfile(final @Valid Profile profile) {
        var newEntity = mapper.apiToEntity(profile);
        var savedEntity = repository.save(newEntity);
        log.debug("createProfile: entity created for userID: {}", profile.userId());
        return mapper.entityToApi(savedEntity);
    }

    @Override
    public void deleteProfile(final Long userId) {
        log.debug("deleteProfile: tries to delete an entity with userId: {}", userId);
        repository.findById(userId).ifPresent(repository::delete);
    }

    @Override
    public Profile getProfile(final Long userId) {
        log.debug("getProfile: tries to get an entity with userId: {}", userId);

        if (userId < 1) {
            throw new InvalidInputException("Invalid userId:" + userId);
        }

        var entity = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("No Profile found for userId: " + userId)
        );

        var response = mapper.entityToApi(entity);

        log.debug("getProfile: found userId: {}", response.userId());
        return response;
    }

    @Override
    public Profile updateProfile(final Long userId, final UpdateProfile profile) {
        log.debug("updateProfile: tries to get an entity with userId: {}", userId);

        var entity = repository.findById(userId).orElseThrow(
                () -> new NotFoundException("No Profile found for userId: " + userId)
        );

        entity.setDisplayName(profile.displayName());
        entity.setBio(profile.bio());
        entity.setLocation(profile.location());

        var savedEntity = repository.save(entity);
        var response = mapper.entityToApi(savedEntity);

        log.debug("updateProfile: modified an entity with userId: {}", response.userId());

        return response;
    }
}
