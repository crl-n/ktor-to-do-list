package com.carlnysten.routing

import com.carlnysten.models.dto.UserSession
import com.carlnysten.plugins.getAuthenticatedUser
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*

fun Route.addAuthRoutes() {
    route("/auth") {
        authenticate("auth-form") {
            post("/login") {
                val user = call.getAuthenticatedUser()

                call.sessions.set(UserSession(user.id, user.username))
                call.respond(HttpStatusCode.OK, "Successfully logged in")
            }

        }

        authenticate ("auth-session") {
            get("/logout") {
                call.sessions.clear<UserSession>()
                call.respond(HttpStatusCode.OK, "Successfully logged out")
            }
        }
    }
}
