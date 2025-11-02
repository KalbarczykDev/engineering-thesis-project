package dev.kalbarczyk.userservice.service;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.core.user.UserService;
import dev.kalbarczyk.util.http.ServiceUtil;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServiceImpl implements UserService {

    private final ServiceUtil serviceUtil;

    public UserServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public User getUser(int userID) {
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
