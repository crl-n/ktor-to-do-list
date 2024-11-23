package com.carlnysten.routing

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.addHealthRoutes() {
    route("/health") {
        get("/live") {
            call.respond(HttpStatusCode.OK, "Application is running")
        }

        get("/ready") {
            try {
                transaction { exec("SELECT 1") }
                call.respond(HttpStatusCode.OK, "Application is ready")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.ServiceUnavailable, "Application is unavailable")
            }
        }
    }
}
