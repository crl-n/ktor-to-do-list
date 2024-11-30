package com.carlnysten.testcontainers

import org.testcontainers.containers.PostgreSQLContainer

fun getPostgresContainer(): PostgreSQLContainer<Nothing> {
    return PostgreSQLContainer<Nothing>("postgres:17.1")
        .apply {
            withDatabaseName("todolist")
            withUsername("postgres")
            withPassword("postgres")
            start()
        }
}
