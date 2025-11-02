package dev.kalbarczyk.profileservice.services;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.ProfileService;
import org.springframework.web.bind.annotation.RestController;
import dev.kalbarczyk.util.http.ServiceUtil;

@RestController
public class ProfileServiceImpl implements ProfileService {

    private final ServiceUtil serviceUtil;

    public ProfileServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Profile getProfile(int userID) {
        return new Profile(
                1,
                "Hardcoded User",
                "https://example.com/avatar.jpg",
                "This is a hardcoded bio.",
                "Earth");
    }
}
