package dev.kalbarczyk.userservice.service;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.core.user.UserService;
import dev.kalbarczyk.api.exceptions.InvalidInputException;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.userservice.persistence.UserRepository;
import dev.kalbarczyk.util.http.ServiceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper mapper;

    private final ServiceUtil serviceUtil;


    @Override
    public User createUser(User body) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }

    @Override
    public User getUser(int userId) {
        return null;
    }

    @Override
    public User updateUser(int userId, User body) {
        return null;
    }
}
