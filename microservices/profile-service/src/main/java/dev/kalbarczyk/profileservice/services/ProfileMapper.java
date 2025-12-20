package dev.kalbarczyk.profileservice.services;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import dev.kalbarczyk.util.DateTimeUtil;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public Profile entityToApi(final ProfileEntity entity) {
        if (entity == null) return null;

        return new Profile(
                entity.getUserId(),
                entity.getDisplayName(),
                entity.getBio(),
                entity.getLocation(),
                DateTimeUtil.toString(entity.getCreatedAt()),
                DateTimeUtil.toString(entity.getUpdatedAt())
        );
    }

    public ProfileEntity apiToEntity(Profile api) {
        if (api == null) return null;

        ProfileEntity entity = new ProfileEntity();
        entity.setUserId(api.userId());
        entity.setDisplayName(api.displayName());
        entity.setBio(api.bio());
        entity.setLocation(api.location());
        entity.setCreatedAt(DateTimeUtil.toLocalDateTime(api.createdAt()));
        entity.setUpdatedAt(DateTimeUtil.toLocalDateTime(api.updatedAt()));
        return entity;
    }
}