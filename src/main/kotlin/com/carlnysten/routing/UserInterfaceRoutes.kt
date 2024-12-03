package com.carlnysten.routing

import com.carlnysten.models.dto.TaskResponseDTO
import com.carlnysten.models.dto.UserResponseDTO
import com.carlnysten.plugins.getAuthenticatedUser
import com.carlnysten.repositories.TaskRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.pebble.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.addUserInterfaceRoutes() {
    val taskRepository by application.inject<TaskRepository>()

    route("/") {

        get("/login") {
            call.respond(HttpStatusCode.OK, PebbleContent(
                "login.html",
                mapOf()
            ))
        }

        authenticate("auth-session") {
            get {
                val user = call.getAuthenticatedUser().let(UserResponseDTO::from)
                val tasks = taskRepository.findAllByUserId(user.id).map(TaskResponseDTO::from)

                call.respond(HttpStatusCode.OK, PebbleContent(
                    "index.html",
                    mapOf("user" to user, "tasks" to tasks)
                ))
            }

        }
    }
}
