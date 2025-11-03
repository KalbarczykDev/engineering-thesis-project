package dev.kalbarczyk.userservice.service;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.core.user.UserService;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    private final ServiceUtil serviceUtil;

    public UserServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public User getUser(int userId) {

        LOG.debug("/users return the found user for userId={}", userId);

        if (userId < 1) {
            throw new InvalidInputException("Invalid userId: " + userId);
        }

        if (userId == 13) {
            throw new NotFoundException("No user found for userId: " + userId);
        }

        return new User(
                1,
                "Hardcoded User",
                "hardcoded@hard.com",
                "password",
                "2024-01-01T00:00:00Z",
                "2024-01-01T00:00:00Z"
        );
    }
}
