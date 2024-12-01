package com.carlnysten.routing

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.addAuthRoutes() {
    route("/auth") {
        authenticate("auth-form") {
            post("/login") {
                call.respond(HttpStatusCode.OK, "Successfully logged in")
            }
        }
    }
}
