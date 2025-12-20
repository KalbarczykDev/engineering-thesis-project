package dev.kalbarczyk.userservice.service;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.userservice.persistence.UserEntity;
import dev.kalbarczyk.util.DateTimeUtil;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User entityToApi(UserEntity entity) {
        if (entity == null) return null;

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getSlug(),
                entity.getEmail(),
                entity.getPassword(),
                DateTimeUtil.toString(entity.getCreatedAt()),
                DateTimeUtil.toString(entity.getUpdatedAt())
        );
    }

    public UserEntity apiToEntity(User api) {
        if (api == null) return null;

        var entity = new UserEntity();
        entity.setId(api.userId());
        entity.setUsername(api.username());
        entity.setSlug(api.slug());
        entity.setEmail(api.email());
        entity.setPassword(api.password());
        entity.setCreatedAt(DateTimeUtil.toLocalDateTime(api.createdAt()));
        entity.setUpdatedAt(DateTimeUtil.toLocalDateTime(api.updatedAt()));
        return entity;
    }
}
