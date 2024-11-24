package com.carlnysten.plugins

import com.carlnysten.exceptions.AuthenticationException
import com.carlnysten.routing.addHealthRoutes
import com.carlnysten.routing.addTaskCollectionRoutes
import com.carlnysten.routing.addTaskRoutes
import com.carlnysten.routing.addUserRoutes
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<AuthenticationException> { call, _ ->
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        addHealthRoutes()
        addUserRoutes()
        addTaskCollectionRoutes()
        addTaskRoutes()
    }
}
