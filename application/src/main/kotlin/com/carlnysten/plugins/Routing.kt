package com.carlnysten.plugins

import com.carlnysten.exceptions.AuthenticationException
import com.carlnysten.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<AuthenticationException> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<BadRequestException> { call, _ ->
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        addHealthRoutes()
        addAuthRoutes()
        addUserRoutes()
        addTaskCollectionRoutes()
        addTaskRoutes()
        addUserInterfaceRoutes()

        staticResources("/static", "static")
    }
}
