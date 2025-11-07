package dev.kalbarczyk.profileservice.config;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    @Bean
    public GridFSBucket gridFsBucket(MongoDatabaseFactory dbFactory) {
        return GridFSBuckets.create(dbFactory.getMongoDatabase(), "avatars");
    }
}