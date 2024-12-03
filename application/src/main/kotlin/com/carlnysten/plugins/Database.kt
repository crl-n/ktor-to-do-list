package com.carlnysten.plugins

import com.carlnysten.config.DatabaseConfig
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase(config: DatabaseConfig): Database {
    return Database.connect(
        config.jdbcUrl,
        driver = "org.postgresql.Driver",
        config.user,
        config.password
    )
}
