package dev.kalbarczyk.profileservice.services;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Mapping(target = "createdAt",
            expression = "java(entity.getCreatedAt() != null ? entity.getCreatedAt().format(FORMATTER) : null)")
    @Mapping(target = "updatedAt",
            expression = "java(entity.getUpdatedAt() != null ? entity.getUpdatedAt().format(FORMATTER) : null)")
    Profile entityToApi(ProfileEntity entity);

    @Mapping(target = "createdAt", expression = "java(api.createdAt() != null ? java.time.LocalDateTime.parse(api.createdAt()) : null)")
    @Mapping(target = "updatedAt", expression = "java(api.updatedAt() != null ? java.time.LocalDateTime.parse(api.updatedAt()) : null)")
    @Mapping(target = "version", ignore = true)
    ProfileEntity apiToEntity(Profile api);

    List<Profile> entityToApi(List<ProfileEntity> entities);

    List<ProfileEntity> apiToEntity(List<Profile> entities);
}
