package com.carlnysten.routing

import com.carlnysten.models.dto.CreateTaskDTO
import com.carlnysten.models.dto.PatchTaskDTO
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
        authenticate("auth-basic", "auth-session") {
            get {
                val user = call.getAuthenticatedUser()

                val tasks = taskRepository.findAllByUserId(user.id)
                    .map(TaskResponseDTO::from)

                call.respond(HttpStatusCode.OK, tasks)
            }

            post {
                val createTaskDTO = call.receive<CreateTaskDTO>()
                val user = call.getAuthenticatedUser()

                taskRepository.addForUserId(createTaskDTO, user.id)

                call.respond(HttpStatusCode.Created, "Task successfully created")
            }

            patch("/{taskId}") {
                val taskId = call.parameters["taskId"]?.toIntOrNull()
                    ?: return@patch call.respond(HttpStatusCode.BadRequest)

                val patchTaskDto = call.receive<PatchTaskDTO>()

                val updatedTask = taskRepository.updateByTaskId(taskId, patchTaskDto)
                    ?.let(TaskResponseDTO::from)
                    ?: return@patch call.respond(HttpStatusCode.InternalServerError)

                call.respond(HttpStatusCode.OK, updatedTask)
            }
        }
    }
}
