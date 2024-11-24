package com.fourthwall.assignment.support

import org.springframework.stereotype.Component


@Component
class DbContainer {
    private val container = PostgreSQLContainer("postgres:17.2")
        .withInitScript("infrastructure/postgres/init.sql")

    init {
        container.start()

        System.setProperty("spring.datasource.url", container.jdbcUrl)
    }
}

class PostgreSQLContainer(image: String) : org.testcontainers.containers.PostgreSQLContainer<PostgreSQLContainer>(image)
