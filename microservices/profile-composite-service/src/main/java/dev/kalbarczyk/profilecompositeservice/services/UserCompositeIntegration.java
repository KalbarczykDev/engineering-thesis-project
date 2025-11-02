package dev.kalbarczyk.profilecompositeservice.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.util.http.HttpErrorInfo;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class UserCompositeIntegration {

    private static final Logger LOG = LoggerFactory.getLogger(UserCompositeIntegration.class);

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
        this.profileServiceUrl = "https://" + profileServiceHost + ":" + profileServicePort + "/profile/";
        this.userServiceUrl = "https://" + userServiceHost + ":" + userServicePort + "/user/";
    }

    public User getUser(final int userId) {
        return getObject(userServiceUrl + userId, User.class);
    }

    public Profile getProfile(final int userId) {
        return getObject(profileServiceUrl + userId, Profile.class);
    }

    private <T> T getObject(final String url, final Class<T> responseType) {
        try {
            LOG.debug("Calling service on URL: {}", url);
            var response = restTemplate.getForObject(url, responseType);
            LOG.debug("Received response from {}", url);
            return response;
        } catch (HttpClientErrorException ex) {
            var status = HttpStatus.resolve(ex.getStatusCode().value());
            if (status == HttpStatus.NOT_FOUND)
                throw new NotFoundException(getErrorMessage(ex));
            if (status == HttpStatus.UNPROCESSABLE_ENTITY)
                throw new InvalidInputException(getErrorMessage(ex));

            LOG.warn("Unexpected HTTP error: {}, body: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }

    private String getErrorMessage(final HttpClientErrorException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).message();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
