package com.mono.wallet;

import com.zaxxer.hikari.HikariDataSource;

import jakarta.activation.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@ActiveProfiles("test")
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test");
        container.start();
        return container;
    }

    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgresContainer) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(postgresContainer.getJdbcUrl());
        dataSource.setUsername(postgresContainer.getUsername());
        dataSource.setPassword(postgresContainer.getPassword());
        return (DataSource) dataSource;
    }
}

