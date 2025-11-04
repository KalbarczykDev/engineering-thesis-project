package dev.kalbarczyk.profileservice.services;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {




    @Override
    public Profile createProfile(Profile profile) {
        return null;
    }

    @Override
    public void deleteProfile(Long userID) {

    }

    @Override
    public Profile getProfile(int userID) {
        return null;
    }

    @Override
    public Profile updateProfile(int userID, Profile profile) {
        return null;
    }
}
