package dev.kalbarczyk.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("dev.kalbarczyk")
@Slf4j
public class UserServiceApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(UserServiceApplication.class, args);
        var mysqlUri = context.getEnvironment().getProperty("spring.datasource.url");
        log.info("Connected to MySQL: {}", mysqlUri);
    }

}
