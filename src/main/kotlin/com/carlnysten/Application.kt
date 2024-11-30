package com.carlnysten

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val dbName = environment.config.property("database.name").getString()
    val dbPort = environment.config.property("database.port").getString()
    val dbUser = environment.config.property("database.user").getString()
    val dbPassword = environment.config.property("database.password").getString()


    val dbConfig = DatabaseConfig(
        jdbcUrl = "jdbc:postgresql://localhost:${dbPort}/${dbName}",
        user = dbUser,
        password = dbPassword,
    )

    configureKoin()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureDatabase(dbConfig)
    runFlyway(dbConfig)
}
