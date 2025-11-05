package dev.kalbarczyk.profileservice;

import static org.junit.jupiter.api.Assertions.*;

import dev.kalbarczyk.api.core.profile.Profile;
import dev.kalbarczyk.profileservice.services.ProfileMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collections;

public class MapperTests {


    private final ProfileMapper mapper = Mappers.getMapper(ProfileMapper.class);

    @Test
    void mapperTests() {

        assertNotNull(mapper);

        var api = new Profile(1L,
                "DisplayName",
                "Bio",
                "Location",
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString());

        var entity = mapper.apiToEntity(api);

        assertEquals(api.userId(), entity.getUserId());
        assertEquals(api.displayName(), entity.getDisplayName());
        assertEquals(api.bio(), entity.getBio());
        assertEquals(api.location(), entity.getLocation());

        var result = mapper.entityToApi(entity);

        assertEquals(api.userId(), result.userId());
        assertEquals(api.displayName(), result.displayName());
        assertEquals(api.bio(), result.bio());
        assertEquals(api.location(), result.location());
        assertEquals(api.createdAt(), result.createdAt());
        assertEquals(api.updatedAt(), result.updatedAt());
    }

    @Test
    void mapperListTests(){
        assertNotNull(mapper);

        var api = new Profile(1L,
                "DisplayName",
                "Bio",
                "Location",
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString());

        var apiList = Collections.singletonList(api);

        var entityList = mapper.apiToEntity(apiList);
        assertEquals(apiList.size(), entityList.size());

        var entity = entityList.getFirst();

        assertEquals(api.userId(), entity.getUserId());
        assertEquals(api.displayName(), entity.getDisplayName());
        assertEquals(api.bio(), entity.getBio());
        assertEquals(api.location(), entity.getLocation());
    }
}
