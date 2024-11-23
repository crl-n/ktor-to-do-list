package com.carlnysten.routing

import com.carlnysten.models.dto.CreateTaskDTO
import com.carlnysten.models.dto.TaskResponseDTO
import com.carlnysten.plugins.getAuthenticatedUser
import com.carlnysten.repositories.TaskRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.addTaskRoutes() {
    val taskRepository by application.inject<TaskRepository>()

    route("/tasks") {
        authenticate("auth-basic") {
            get {
                val user = call.getAuthenticatedUser()

                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, "User doesn't exist")
                    return@get
                }

                val tasks = taskRepository.findAllByUserId(user.id)
                val taskDtos = tasks.map(TaskResponseDTO::from)
                call.respond(HttpStatusCode.OK, taskDtos)
            }

            post {
                val createTaskDTO = call.receive<CreateTaskDTO>()
                val user = call.getAuthenticatedUser()

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
