package com.carlnysten.testcontainers

import org.testcontainers.containers.PostgreSQLContainer
import java.sql.DriverManager

fun getPostgresContainer(): PostgreSQLContainer<Nothing> {
    return PostgreSQLContainer<Nothing>("postgres:17.1")
        .apply {
            withDatabaseName("todolist")
            withUsername("postgres")
            withPassword("postgres")
            start()
        }
}

fun PostgreSQLContainer<Nothing>.reset() {
    val connection = DriverManager.getConnection(
        jdbcUrl,
        username,
        password
    )

    connection.createStatement().execute(
        "TRUNCATE TABLE app.tasks, app.users, app.task_collections RESTART IDENTITY CASCADE;"
    )

    connection.close()
}
