package dev.kalbarczyk.profileservice;

import dev.kalbarczyk.profileservice.persistence.ProfileEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import reactor.core.publisher.Hooks;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
@ComponentScan("dev.kalbarczyk")
public class ProfileServiceApplication {

    private final ReactiveMongoOperations mongoTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {
        MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> mappingContext =
                mongoTemplate.getConverter().getMappingContext();
        IndexResolver resolver = new MongoPersistentEntityIndexResolver(mappingContext);

        var indexOps = mongoTemplate.indexOps(ProfileEntity.class);
        resolver.resolveIndexFor(ProfileEntity.class).forEach(e -> indexOps.createIndex(e).block());
    }

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        var ctx = SpringApplication.run(ProfileServiceApplication.class, args);
        var mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
        var mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
        log.info("Connected to MongoDb: {}:{}", mongodDbHost, mongodDbPort);

    }

}
