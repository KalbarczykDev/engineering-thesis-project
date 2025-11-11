package dev.kalbarczyk.usercompositeservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.core.user.CreateUser;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.event.Event;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import dev.kalbarczyk.util.http.HttpErrorInfo;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.io.IOException;
import java.util.logging.Level;

import static java.util.logging.Level.FINE;

@Component
@Slf4j
public class UserCompositeIntegration {

    private final WebClient webClient;
    private final ObjectMapper mapper;

    private final String profileServiceUrl;
    private final String userServiceUrl;

    private final StreamBridge streamBridge;

    private final Scheduler publishEventScheduler;

    public UserCompositeIntegration(
            final @Qualifier("publishEventScheduler") Scheduler publishEventScheduler,
            final WebClient.Builder webClient,
            final ObjectMapper mapper,
            final StreamBridge streamBridge,
            final @Value("${app.user-service.host}") String userServiceHost,
            final @Value("${app.user-service.port}") int userServicePort,
            final @Value("${app.profile-service.host}") String profileServiceHost,
            final @Value("${app.profile-service.port}") int profileServicePort
    ) {
        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClient.build();
        this.mapper = mapper;
        this.streamBridge = streamBridge;
        this.profileServiceUrl = "http://" + profileServiceHost + ":" + profileServicePort + "/profiles";
        this.userServiceUrl = "http://" + userServiceHost + ":" + userServicePort + "/users";
    }

    public Mono<Tuple2<User, Profile>> createUserAndProfile(final CreateUser body) {
        return webClient.post()
                .uri(userServiceUrl)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(User.class)
                .flatMap(user -> {
                    var minimalProfile = new Profile(
                            user.userId(),
                            user.username(),
                            "I am " + user.username(),
                            null,
                            null,
                            null
                    );
                    return webClient.post()
                            .uri(profileServiceUrl)
                            .bodyValue(minimalProfile)
                            .retrieve()
                            .bodyToMono(Profile.class)
                            .map(profile -> Tuples.of(user, profile));
                })
                .log(log.getName(), FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException)
                .subscribeOn(publishEventScheduler);
    }

    public Mono<User> getUser(final Long userId) {
        var url = userServiceUrl + "/" + userId;
        log.debug("Will get a user from URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(User.class).log(log.getName(), Level.FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    public Mono<Profile> getProfile(final Long userId) {
        var url = profileServiceUrl + "/" + userId;
        log.debug("Will get a profile from URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(Profile.class).log(log.getName(), FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }


    public Mono<Profile> updateProfile(final Long userId, final UpdateProfile body) {
        var url = profileServiceUrl + "/" + userId;
        log.debug("Will update a profile on URL: {}", url);
        return webClient.put().uri(url)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Profile.class)
                .log(log.getName(), FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }


    public Mono<Void> deleteUser(final Long userId) {
        log.debug("deleteUser: sending DELETE event for userId: {}", userId);
        return Mono.fromRunnable(() -> sendMessage("users-out-0", new Event<>(Event.Type.DELETE, userId, null)))
                .subscribeOn(publishEventScheduler)
                .then();
    }

    public Mono<Void> deleteProfile(final Long userId) {
        log.debug("deleteProfile: sending DELETE event for userId: {}", userId);
        return Mono.fromRunnable(() -> sendMessage("profiles-out-0", new Event<>(Event.Type.DELETE, userId, null)))
                .subscribeOn(publishEventScheduler)
                .then();
    }


    public Mono<Health> getProfileHealth() {
        return getHealth(profileServiceUrl);
    }

    public Mono<Health> getUserHealth() {
        return getHealth(userServiceUrl);
    }

    private Mono<Health> getHealth(String url) {
        url += "/actuator/health";
        log.debug("Will call the Health API on URL: {}", url);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class)
                .map(_ -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log(log.getName(), FINE);
    }


    private void sendMessage(final String bindingName, @SuppressWarnings("rawtypes") final Event event) {
        log.debug("Sending a {} message to {}", event.getEventType(), bindingName);
        var message = MessageBuilder.withPayload(event)
                .setHeader("partitionKey", event.getKey())
                .build();
        streamBridge.send(bindingName, message);
    }

    private Throwable handleException(final Throwable ex) {

        if (!(ex instanceof WebClientResponseException wcre)) {
            log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        switch (HttpStatus.resolve(wcre.getStatusCode().value())) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(wcre));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(wcre));

            case null:
            default:
                log.warn("Got an unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                log.warn("Error body: {}", wcre.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(final WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }

}
