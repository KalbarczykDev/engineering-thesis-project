package se.magnus.springcloud.configserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication {

    private static final Logger log = LoggerFactory.getLogger(ConfigServerApplication.class);

    public static void main(String[] args) {
        var ctx = SpringApplication.run(ConfigServerApplication.class, args);
        var repoLocation = ctx.getEnvironment().getProperty("spring.cloud.config.server.native.searchLocations");
        log.info("Serving configurations from folder: {}", repoLocation);
    }

}
