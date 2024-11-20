package com.carlnysten.routing

import com.carlnysten.models.dto.CreateUserDTO
import com.carlnysten.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

val userRepository = UserRepository()

fun Route.addUserRoutes() {
    route("users") {
        post {
            userRepository.add(CreateUserDTO("test", "test"))
            call.respond(HttpStatusCode.Created)
        }
        authenticate("auth-basic") {
            get {
                val users = userRepository.findAll()
                call.respond(HttpStatusCode.OK, users)
            }
        }
    }
}
