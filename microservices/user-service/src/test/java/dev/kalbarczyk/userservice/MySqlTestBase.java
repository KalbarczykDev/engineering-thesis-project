package dev.kalbarczyk.userservice;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;


@SuppressWarnings("resource")
public abstract class MySqlTestBase {

    @SuppressWarnings("rawtypes")
    @ServiceConnection
    static final JdbcDatabaseContainer database = new MySQLContainer("mysql:9.2.0").withStartupTimeoutSeconds(300);

    static {
        database.start();
    }
}
