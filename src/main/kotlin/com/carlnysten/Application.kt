package com.carlnysten

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dbConfig = DatabaseConfig(
        jdbcUrl = "jdbc:postgresql://localhost:5432/todolist",
        user = "postgres",
        password = "postgres"
    )

    configureKoin()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureDatabase(dbConfig)
    runFlyway(dbConfig)
}
