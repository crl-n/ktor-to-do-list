package com.carlnysten.plugins

import io.ktor.server.application.Application
import org.flywaydb.core.Flyway

fun Application.runFlyway() {
    Flyway.configure()
        .dataSource(
            "jdbc:postgresql://localhost:5432/todolist",
            "postgres",
            "postgres"
        )
        .load()
        .migrate()
}
