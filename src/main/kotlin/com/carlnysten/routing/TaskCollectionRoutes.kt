package com.carlnysten.routing

import com.carlnysten.models.dto.CreateTaskCollectionDTO
import com.carlnysten.plugins.getAuthenticatedUser
import com.carlnysten.repositories.TaskCollectionRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.addTaskCollectionRoutes() {
    val taskCollectionRepository by application.inject<TaskCollectionRepository>()

    route("/collections") {
        authenticate("auth-basic") {
            get {
                val user = call.getAuthenticatedUser()

                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                val collections = taskCollectionRepository.findAllByUserId(user.id)
                call.respond(HttpStatusCode.OK, collections)
            }

            post {
                val user = call.getAuthenticatedUser()
                val createCollectionDto = call.receive<CreateTaskCollectionDTO>()

                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }

                taskCollectionRepository.addForUserId(user.id, createCollectionDto)
                call.respond(HttpStatusCode.Created, "Task collection successfully created")
            }
        }
    }
}
