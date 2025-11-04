package dev.kalbarczyk.userservice.service;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.userservice.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mappings(
            {
                    @Mapping(target = "serviceAddress", ignore = true)
            }
    )
    User entityToApi(final UserEntity entity);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    UserEntity apiToEntity(final User api);

    List<User> entityListToApiList(final List<User> entity);

    List<UserEntity> apiListToEntityList(final List<User> api);
}
