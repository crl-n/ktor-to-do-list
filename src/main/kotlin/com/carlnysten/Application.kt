package com.carlnysten

import com.carlnysten.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureKoin()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureDatabase()
    runFlyway()
}
