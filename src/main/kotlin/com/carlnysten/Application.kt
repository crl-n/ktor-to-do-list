package com.carlnysten

import com.carlnysten.plugins.configureRouting
import com.carlnysten.plugins.configureSecurity
import com.carlnysten.plugins.configureSerialization
import com.carlnysten.plugins.runFlyway
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSecurity()
    configureSerialization()
    configureRouting()
    runFlyway()
}
