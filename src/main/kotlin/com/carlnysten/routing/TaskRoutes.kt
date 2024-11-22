package com.carlnysten.routing

import com.carlnysten.models.dto.CreateTaskDTO
import com.carlnysten.repositories.TaskRepository
import com.carlnysten.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.addTaskRoutes() {
    val taskRepository by application.inject<TaskRepository>()
    val userRepository by application.inject<UserRepository>()

    route("/tasks") {
        authenticate("auth-basic") {
            post {
                val createTaskDTO = call.receive<CreateTaskDTO>()
                val principal = call.principal<UserIdPrincipal>()

                if (principal == null) {
                    call.respond(HttpStatusCode.Unauthorized, "Unauthorized request")
                    return@post
                }

                val username = principal.name
                val user = userRepository.findByUsername(username)

                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, "User doesn't exist")
                    return@post
                }

                taskRepository.addForUserId(createTaskDTO, user.id)
                call.respond(HttpStatusCode.Created, "Task successfully created")
            }
        }
    }
}
