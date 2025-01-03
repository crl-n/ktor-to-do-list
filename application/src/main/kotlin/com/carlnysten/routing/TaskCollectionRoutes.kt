package com.carlnysten.routing

import com.carlnysten.models.dto.CreateTaskCollectionDTO
import com.carlnysten.models.dto.TaskCollectionResponseDTO
import com.carlnysten.models.dto.TaskIdDTO
import com.carlnysten.plugins.getAuthenticatedUser
import com.carlnysten.repositories.TaskCollectionRepository
import com.carlnysten.repositories.TaskRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.addTaskCollectionRoutes() {
    val taskCollectionRepository by application.inject<TaskCollectionRepository>()
    val taskRepository by application.inject<TaskRepository>()

    route("/collections") {
        authenticate("auth-basic") {
            get {
                val user = call.getAuthenticatedUser()

                val collections = taskCollectionRepository.findAllByUserId(user.id)
                    .map(TaskCollectionResponseDTO::from)

                call.respond(HttpStatusCode.OK, collections)
            }

            post {
                val user = call.getAuthenticatedUser()
                val createCollectionDto = call.receive<CreateTaskCollectionDTO>()

                taskCollectionRepository.addForUserId(user.id, createCollectionDto)

                call.respond(HttpStatusCode.Created, "Task collection successfully created")
            }

            post("/{collectionId}/tasks") {
                try {
                    val user = call.getAuthenticatedUser()
                    val dto = call.receive<TaskIdDTO>()

                    val collectionId = call.parameters["collectionId"]?.toInt()
                        ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                            "Collection id parameter is required"
                        )

                    taskRepository.assignTaskToCollection(user.id, collectionId, dto.taskId)

                    call.respond(HttpStatusCode.Created, "Task collection successfully assigned")
                } catch (e: NumberFormatException) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid collection id")
                }
            }
        }
    }
}
