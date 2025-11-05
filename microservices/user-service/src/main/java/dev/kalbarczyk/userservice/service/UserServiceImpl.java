package dev.kalbarczyk.userservice.service;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.core.user.UserService;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper mapper;


    @Override
    public User createUser(final @Valid User body) {
        log.debug("createUser: tries to create a new entity for username: {}", body.username());

        repository.findByEmail(body.email())
                .ifPresent(u -> {
                    throw new InvalidInputException("Email already in use: " + body.email());
                });


        var newEntity = mapper.apiToEntity(body);
        var savedEntity = repository.save(newEntity);
        log.debug("createUser: entity created for userId: {}", body.userId());
        return mapper.entityToApi(savedEntity);
    }

    @Override
    public void deleteUser(final Long userId) {
        log.debug("deleteUser: tries to delete an entity with userId: {}", userId);
        repository.findById(userId).ifPresent(repository::delete);
    }

    @Override
    public User getUser(final Long userId) {
        log.debug("getUser: tries to get an entity with userId: {}", userId);
        if (userId < 1) {
            throw new InvalidInputException("Invalid userId: " + userId);
        }

        var entity = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found for userId: " + userId));

        var response = mapper.entityToApi(entity);

        log.debug("getUser: found userId: {}", response.userId());
        return response;
    }

    @Override
    public User updateUser(final Long userId, final @Valid User body) {
        log.debug("updateUser: tries to modify an entity with userId: {}", userId);

        var entity = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("No user found for userId: " + userId));

        repository.findByEmail(body.email())
                .filter(u -> !u.getId().equals(userId))
                .ifPresent(u -> {
                    throw new InvalidInputException("Email already in use: " + body.email());
                });

        entity.setUsername(body.username());
        entity.setEmail(body.email());
        entity.setPassword(body.password());

        var updated = repository.save(entity);
        var response = mapper.entityToApi(updated);

        log.debug("updateUser: modified an entity with userId: {}", response.userId());

        return response;
    }
}
