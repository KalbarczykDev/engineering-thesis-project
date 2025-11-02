package dev.kalbarczyk.profilecompositeservice.services;

import dev.kalbarczyk.api.core.composite.user.UserAggregate;
import dev.kalbarczyk.api.core.composite.user.UserCompositeService;
import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.api.exceptions.NotFoundException;
import dev.kalbarczyk.util.http.ServiceUtil;

public class UserCompositeServiceImpl implements UserCompositeService {

    private final ServiceUtil serviceUtil;
    private UserCompositeIntegration integration;

    public UserCompositeServiceImpl(
            ServiceUtil serviceUtil, UserCompositeIntegration integration) {

        this.serviceUtil = serviceUtil;
        this.integration = integration;
    }

    @Override
    public UserAggregate getUser(int userId) {

        var user = integration.getUser(userId);
        if (user == null) {
            throw new NotFoundException("No user found for userId: " + userId);
        }

        var profile = integration.getProfile(userId);

        if (profile == null) {
            throw new NotFoundException("No profile found for userId: " + userId);
        }

        return null;
    }

    private UserAggregate createUserAggregate(
            final User user, final Profile profile, final String serviceAddress
    ) {


        return null;
    }

}
