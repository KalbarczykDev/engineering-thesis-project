package dev.kalbarczyk.userservice.service;

import dev.kalbarczyk.api.core.user.CreateUser;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.core.user.UserService;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.userservice.persistence.UserEntity;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import dev.kalbarczyk.util.SlugUtil;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;


@RestController
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository repository;

    private final UserMapper mapper;

    public UserServiceImpl(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<User> createUser(final @Valid CreateUser body) {
        return Mono.fromCallable(() -> {
                    repository.findByEmailOrUsername(body.email(), body.username())
                            .ifPresent(u -> {
                                if (u.getEmail().equals(body.email()))
                                    throw new InvalidInputException("Email already in use: " + body.email());
                                if (u.getUsername().equals(body.username()))
                                    throw new InvalidInputException("Username already in use: " + body.username());
                            });
                    var newUser = new UserEntity();
                    newUser.setUsername(body.username());
                    newUser.setSlug(SlugUtil.toSlug(body.username()));
                    newUser.setEmail(body.email());
                    newUser.setPassword(body.password());
                    return newUser;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(newEntity ->
                        Mono.fromCallable(() -> repository.save(newEntity))
                                .subscribeOn(Schedulers.boundedElastic())
                                .map(mapper::entityToApi)
                );

    }

    @Override
    public Mono<Void> deleteUser(final Long userId) {
        return Mono.fromRunnable(() -> {
                    log.debug("deleteUser: tries to delete an entity with userId: {}", userId);
                    repository.findById(userId).ifPresent(repository::delete);
                })
                .subscribeOn(Schedulers.boundedElastic()).then();
    }

    @Override
    public Mono<User> getUser(final Long userId) {
        if (userId < 1) {
            return Mono.error(new InvalidInputException("Invalid userId: " + userId));
        }

        return Mono.fromCallable(() -> {
                    var entity = repository.findById(userId)
                            .orElseThrow(() -> new NotFoundException("No user found for userId: " + userId));
                    var response = mapper.entityToApi(entity);
                    log.debug("getUser: found userId: {}", response.userId());
                    return response;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<User> updateUser(final Long userId, final @Valid User body) {
        return Mono.fromCallable(() -> {
                    log.debug("updateUser: tries to modify an entity with userId: {}", userId);

                    var entity = repository.findById(userId)
                            .orElseThrow(() -> new NotFoundException("No user found for userId: " + userId));

                    repository.findByEmailOrUsername(body.email(), body.username())
                            .filter(u -> !u.getId().equals(userId))
                            .ifPresent(u -> {
                                if (u.getEmail().equals(body.email()))
                                    throw new InvalidInputException("Email already in use: " + body.email());
                                if (u.getUsername().equals(body.username()))
                                    throw new InvalidInputException("Username already in use: " + body.username());
                            });

                    entity.setUsername(body.username());
                    entity.setSlug(SlugUtil.toSlug(body.username()));
                    entity.setEmail(body.email());
                    entity.setPassword(body.password());

                    var updated = repository.save(entity);
                    var response = mapper.entityToApi(updated);

                    log.debug("updateUser: modified an entity with userId: {}", response.userId());

                    return response;
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}
