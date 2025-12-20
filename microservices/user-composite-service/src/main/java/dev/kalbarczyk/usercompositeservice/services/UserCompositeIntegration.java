package dev.kalbarczyk.usercompositeservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.UpdateProfile;
import dev.kalbarczyk.api.core.user.CreateUser;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.event.Event;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.api.exceptions.ServiceUnavailableException;
import dev.kalbarczyk.util.http.HttpErrorInfo;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
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
public class UserCompositeIntegration {

    private static final Logger log = LoggerFactory.getLogger(UserCompositeIntegration.class);

    private final SecurityContext nullSecCtx = new SecurityContextImpl();

    private final WebClient webClient;
    private final ObjectMapper mapper;

    private final String profileServiceUrl = "http://profile/profiles";
    private final String userServiceUrl = "http://user/users";

    private final StreamBridge streamBridge;

    private final Scheduler publishEventScheduler;

    public UserCompositeIntegration(
            final @Qualifier("publishEventScheduler") Scheduler publishEventScheduler,
            final WebClient webClient,
            final ObjectMapper mapper,
            final StreamBridge streamBridge
    ) {
        this.publishEventScheduler = publishEventScheduler;
        this.webClient = webClient;
        this.mapper = mapper;
        this.streamBridge = streamBridge;
    }

    public Mono<Tuple2<User, Profile>> createUserAndProfile(final CreateUser body) {
        log.debug("Sending request to create user: {}", body);

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

    @Retry(name = "user")
    @TimeLimiter(name = "user")
    @CircuitBreaker(name = "user", fallbackMethod = "getUserFallbackValue")
    public Mono<User> getUser(final Long userId) {
        var url = userServiceUrl + "/" + userId;
        log.debug("Will get a user from URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(User.class).log(log.getName(), Level.FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    private Mono<User> getUserFallbackValue(final Long userId, final CallNotPermittedException ex) {
        return throwServiceUnavailable("user", userId);
    }

    @Retry(name = "profile")
    @TimeLimiter(name = "profile")
    @CircuitBreaker(name = "profile", fallbackMethod = "getProfileFallbackValue")
    public Mono<Profile> getProfile(final Long userId) {
        var url = profileServiceUrl + "/" + userId;
        log.debug("Will get a profile from URL: {}", url);

        return webClient.get().uri(url).retrieve().bodyToMono(Profile.class).log(log.getName(), FINE)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    private Mono<Profile> getProfileFallbackValue(final Long userId, final CallNotPermittedException ex) {
        return throwServiceUnavailable("profile", userId);
    }


    @Retry(name = "profile-update")
    @TimeLimiter(name = "profile-update")
    @CircuitBreaker(name = "profile-update", fallbackMethod = "updateProfileFallbackValue")
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

    private Mono<Profile> updateProfileFallbackValue(
            final Long userId,
            final UpdateProfile body,
            final CallNotPermittedException ex) {
        return throwServiceUnavailable("profile-update", userId);
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

    private <T> Mono<T> throwServiceUnavailable(final String serviceName, final Long userId) {
        var errorMessage = String.format(
                "Circuit Breaker is OPEN for the %s service. Failed to retrieve resource with ID: %d. Returning 503 SERVICE UNAVAILABLE.",
                serviceName,
                userId
        );
        log.warn("Resilience Fallback: {}", errorMessage);
        throw new ServiceUnavailableException(errorMessage);
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

            case BAD_REQUEST:
                return new IllegalArgumentException(getErrorMessage(wcre));

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

    private Mono<SecurityContext> getLogAuthorizationInfoMono() {
        return getSecurityContextMono().doOnNext(this::logAuthorizationInfo);
    }

    private Mono<SecurityContext> getSecurityContextMono() {
        return ReactiveSecurityContextHolder.getContext().defaultIfEmpty(nullSecCtx);
    }

    private void logAuthorizationInfo(final SecurityContext sc) {
        if (sc != null && sc.getAuthentication() != null && sc.getAuthentication() instanceof JwtAuthenticationToken) {
            var jwtToken = ((JwtAuthenticationToken) sc.getAuthentication()).getToken();
            logAuthorizationInfo(jwtToken);
        } else {
            log.warn("No JWT based Authentication supplied, running tests are we?");
        }
    }

    private void logAuthorizationInfo(final Jwt jwt) {
        if (jwt == null) {
            log.warn("No JWT supplied, running tests are we?");
        } else {
            if (log.isDebugEnabled()) {
                var issuer = jwt.getIssuer();
                var audience = jwt.getAudience();
                var subject = jwt.getClaims().get("sub");
                var scopes = jwt.getClaims().get("scope");
                var expires = jwt.getClaims().get("exp");

                log.debug("Authorization info: Subject: {}, scopes: {}, expires {}: issuer: {}, audience: {}", subject, scopes, expires, issuer, audience);
            }
        }
    }
}