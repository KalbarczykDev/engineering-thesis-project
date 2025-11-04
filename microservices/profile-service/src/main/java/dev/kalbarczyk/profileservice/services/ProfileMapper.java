package dev.kalbarczyk.profileservice.services;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import dev.kalbarczyk.util.mapping.datetime.LocalDateTimeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = LocalDateTimeMapper.class)
public interface ProfileMapper {

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "asString")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "asString")
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
