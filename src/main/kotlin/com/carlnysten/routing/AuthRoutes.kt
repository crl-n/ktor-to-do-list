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
                val userAgent = call.request.headers["User-Agent"] ?: ""

                call.sessions.set(UserSession(user.id, user.username))

                if (userAgent.startsWith("HTTPie")) {
                    call.respond(HttpStatusCode.OK, "Successfully logged in")
                } else {
                    call.respondRedirect("/")
                }
            }
        }

        authenticate ("auth-session") {
            get("/logout") {
                call.sessions.clear<UserSession>()
                val userAgent = call.request.headers["User-Agent"] ?: ""

                if (userAgent.startsWith("HTTPie")) {
                    call.respond(HttpStatusCode.OK, "Successfully logged out")
                } else {
                    call.respondRedirect("/login")
                }
            }
        }
    }
}
