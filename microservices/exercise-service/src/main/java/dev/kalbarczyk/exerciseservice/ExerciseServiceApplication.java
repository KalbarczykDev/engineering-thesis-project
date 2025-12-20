package dev.kalbarczyk.exerciseservice;

import dev.kalbarczyk.exerciseservice.persistence.ExerciseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@ComponentScan("dev.kalbarczyk")
public class ExerciseServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(ExerciseServiceApplication.class);

    private final ReactiveMongoOperations mongoTemplate;

    public ExerciseServiceApplication(ReactiveMongoOperations mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void initIndicesAfterStartup() {
        var mappingContext = mongoTemplate.getConverter().getMappingContext();
        var resolver = new MongoPersistentEntityIndexResolver(mappingContext);
        var indexOps = mongoTemplate.indexOps(ExerciseEntity.class);
        resolver.resolveIndexFor(ExerciseEntity.class).forEach(e -> indexOps.createIndex(e).block());
    }

    public static void main(String[] args) {
        Hooks.enableAutomaticContextPropagation();
        var ctx = SpringApplication.run(ExerciseServiceApplication.class, args);
        var mongodDbHost = ctx.getEnvironment().getProperty("spring.data.mongodb.host");
        var mongodDbPort = ctx.getEnvironment().getProperty("spring.data.mongodb.port");
        log.info("Connected to MongoDb: {}:{}", mongodDbHost, mongodDbPort);

    }

}
