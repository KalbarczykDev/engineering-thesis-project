package dev.kalbarczyk.userservice;

import static org.junit.jupiter.api.Assertions.*;

import dev.kalbarczyk.api.core.user.User;
import dev.kalbarczyk.userservice.service.UserMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collections;

public class MapperTests {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void mapperTests() {
        assertNotNull(mapper);

        var api = new User(1, "name", "email", "password", LocalDateTime.now().toString(), LocalDateTime.now().toString());

        var entity = mapper.apiToEntity(api);

        assertEquals(api.userId(), entity.getId());
        assertEquals(api.username(), entity.getUsername());
        assertEquals(api.email(), entity.getEmail());
        assertEquals(api.password(), entity.getPassword());
        assertEquals(api.createdAt(), entity.getCreatedAt().toString());
        assertEquals(api.updatedAt(), entity.getUpdatedAt().toString());


        var result = mapper.entityToApi(entity);

        assertEquals(api.userId(), result.userId());
        assertEquals(api.username(), result.username());
        assertEquals(api.email(), result.email());
        assertEquals(api.password(), result.password());
        assertEquals(api.createdAt(), result.createdAt());
        assertEquals(api.updatedAt(), result.updatedAt());
    }

    @Test
    void mapperListTests(){
        assertNotNull(mapper);

        var api = new User(1, "name", "email", "password", LocalDateTime.now().toString(), LocalDateTime.now().toString());

        var apiList = Collections.singletonList(api);

        var entityList = mapper.apiListToEntityList(apiList);
        assertEquals(apiList.size(), entityList.size());

        var entity = entityList.getFirst();

        assertEquals(api.userId(), entity.getId());
        assertEquals(api.username(), entity.getUsername());
        assertEquals(api.email(), entity.getEmail());
        assertEquals(api.password(), entity.getPassword());
        assertEquals(api.createdAt(), entity.getCreatedAt().toString());
        assertEquals(api.updatedAt(), entity.getUpdatedAt().toString());

        var resultList = mapper.entityListToApiList(entityList);
        assertEquals(apiList.size(), resultList.size());

        var result = resultList.getFirst();

        assertEquals(api.userId(), result.userId());
        assertEquals(api.username(), result.username());
        assertEquals(api.email(), result.email());
        assertEquals(api.password(), result.password());
        assertEquals(api.createdAt(), result.createdAt());
        assertEquals(api.updatedAt(), result.updatedAt());
    }
}
