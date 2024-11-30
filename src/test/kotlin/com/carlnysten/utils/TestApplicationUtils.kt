package com.carlnysten.utils

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*

fun ApplicationTestBuilder.createClientWithJsonNegotiation(): HttpClient {
    return createClient {
        install(ContentNegotiation) {
            json()
        }
    }
}
