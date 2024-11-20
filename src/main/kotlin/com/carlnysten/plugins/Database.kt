package com.carlnysten.plugins

import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase() {
    Database.connect(
        "jdbc:postgresql://localhost:5432/todolist",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "postgres"
    )
}
