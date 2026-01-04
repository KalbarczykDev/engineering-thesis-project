package dev.kalbarczyk.exerciseservice;

import dev.kalbarczyk.exerciseservice.persistence.ExerciseEntity;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@ComponentScan("dev.kalbarczyk")
@EnableReactiveMongoAuditing
public class ExerciseServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(ExerciseServiceApplication.class);
    private final ReactiveMongoOperations mongoTemplate;

    @Value("${api.common.version}")
    String apiVersion;
    @Value("${api.common.title}")
    String apiTitle;
    @Value("${api.common.description}")
    String apiDescription;
    @Value("${api.common.termsOfService}")
    String apiTermsOfService;
    @Value("${api.common.license}")
    String apiLicense;
    @Value("${api.common.licenseUrl}")
    String apiLicenseUrl;
    @Value("${api.common.externalDocDesc}")
    String apiExternalDocDesc;
    @Value("${api.common.externalDocUrl}")
    String apiExternalDocUrl;
    @Value("${api.common.contact.name}")
    String apiContactName;
    @Value("${api.common.contact.url}")
    String apiContactUrl;
    @Value("${api.common.contact.email}")
    String apiContactEmail;

    /**
     * Will exposed on $HOST:$PORT/swagger-ui.html
     *
     * @return the common OpenAPI documentation
     */
    @Bean
    public OpenAPI getOpenApiDocumentation() {
        return new OpenAPI()
                .info(new Info().title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion)
                        .contact(new Contact()
                                .name(apiContactName)
                                .url(apiContactUrl)
                                .email(apiContactEmail))
                        .termsOfService(apiTermsOfService)
                        .license(new License()
                                .name(apiLicense)
                                .url(apiLicenseUrl)))
                .externalDocs(new ExternalDocumentation()
                        .description(apiExternalDocDesc)
                        .url(apiExternalDocUrl));
    }

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
