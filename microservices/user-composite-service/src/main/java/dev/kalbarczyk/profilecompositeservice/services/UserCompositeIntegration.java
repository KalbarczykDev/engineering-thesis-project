package dev.kalbarczyk.profilecompositeservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import dev.kalbarczyk.util.http.HttpErrorInfo;

import java.io.IOException;

@Component
@Slf4j
public class UserCompositeIntegration {


    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String profileServiceUrl;
    private final String userServiceUrl;

    public UserCompositeIntegration(
            final RestTemplate restTemplate,
            final ObjectMapper objectMapper,
            final @Value("${app.profile-service.host}") String profileServiceHost,
            final @Value("${app.profile-service.port}") int profileServicePort,
            final @Value("${app.user-service.host}") String userServiceHost,
            final @Value("${app.user-service.port}") int userServicePort
    ) {
        this.restTemplate = restTemplate;
        this.mapper = objectMapper;
        this.profileServiceUrl = "http://" + profileServiceHost + ":" + profileServicePort + "/profiles";
        this.userServiceUrl = "http://" + userServiceHost + ":" + userServicePort + "/users";
    }

    public void createUser(final User body) {
        try {
            log.debug("Will post a new user to URL: {}", userServiceUrl);

            var user = restTemplate.postForObject(userServiceUrl, body, User.class);

            log.debug("Will post new profile to URL: {}", profileServiceUrl);

            assert user != null;
            var minimalProfile = new Profile(user.userId(),
                    user.username(),
                    "I am " + user.username(),
                    null,
                    null,
                    null);

            restTemplate.postForObject(profileServiceUrl, minimalProfile, Profile.class);
            log.debug("Created user and profile entities for username: {}", body.username());


        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public User getUser(final Long userId) {
        try {
            var url = userServiceUrl + "/" + userId;
            log.debug("Will get a user from URL: {}", url);

            return restTemplate.getForObject(url, User.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public Profile getProfile(final Long userId) {
        try {
            var url = profileServiceUrl + "/" + userId;
            log.debug("Will get a profile from URL: {}", url);

            return restTemplate.getForObject(url, Profile.class);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteUser(final Long userId) {
        try {
            var userUrl = userServiceUrl + "?userId=" + userId;
            log.debug("Will delete a user from URL: {}", userUrl);
            restTemplate.delete(userUrl);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }

    public void deleteProfile(final Long userId) {
        try {
            var profileUrl = profileServiceUrl + "?userId=" + userId;
            log.debug("Will call the deleteProfile API on URL: {}", profileUrl);
            restTemplate.delete(profileUrl);

        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
        }
    }


    private RuntimeException handleHttpClientException(final HttpClientErrorException ex) {
        switch (HttpStatus.resolve(ex.getStatusCode().value())) {
            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(ex));

            case UNPROCESSABLE_ENTITY:
                return new InvalidInputException(getErrorMessage(ex));
            case null:
            default:
                log.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                log.warn("Error body: {}", ex.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(final HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }


}
