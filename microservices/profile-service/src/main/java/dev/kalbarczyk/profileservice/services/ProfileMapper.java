package dev.kalbarczyk.profileservice.services;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    @Mappings({
            @Mapping(target = "createdAt", expression = "java(entity.getCreatedAt() != null ? entity.getCreatedAt().format(FORMATTER) : null)"),
            @Mapping(target = "updatedAt", expression = "java(entity.getUpdatedAt() != null ? entity.getUpdatedAt().format(FORMATTER) : null)")
    })
    Profile entityToApi(ProfileEntity entity);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "updatedAt", ignore = true),
            @Mapping(target = "version", ignore = true)
    })
    ProfileEntity apiToEntity(Profile api);

    List<Profile> entityToApi(List<ProfileEntity> entities);

    List<ProfileEntity> apiToEntity(List<Profile> entities);
}
