package com.carlnysten

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.models.session.RedisSessionStorage
import com.carlnysten.plugins.*
import io.ktor.server.application.*
import io.lettuce.core.RedisClient

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

    val sessionDurationSeconds: Long = 160
    val redisClient = RedisClient.create("redis://localhost:6379/0")
    val redisConnection = redisClient.connect()
    val redisSessionStorage = RedisSessionStorage(redisConnection, sessionDurationSeconds)

    configureKoin()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureSessions(redisSessionStorage, sessionDurationSeconds)
    configureDatabase(dbConfig)
    runFlyway(dbConfig)
    configureTemplating()
}
