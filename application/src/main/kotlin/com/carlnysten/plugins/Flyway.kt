package com.carlnysten.plugins

import com.carlnysten.config.DatabaseConfig
import io.ktor.server.application.Application
import org.flywaydb.core.Flyway

fun Application.runFlyway(config: DatabaseConfig) {
    Flyway.configure()
        .dataSource(
            config.jdbcUrl,
            config.user,
            config.password
        )
        .load()
        .migrate()
}
