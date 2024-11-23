package com.carlnysten.routing

import com.carlnysten.models.dto.CreateUserDTO
import com.carlnysten.models.dto.UserResponseDTO
import com.carlnysten.plugins.getAuthenticatedUser
import com.carlnysten.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.addUserRoutes() {
    val userRepository by application.inject<UserRepository>()

    route("users") {
        post {
            val createUserDTO = call.receive<CreateUserDTO>()
            userRepository.add(createUserDTO)
            call.respond(HttpStatusCode.Created, "User successfully created")
        }
        authenticate("auth-basic") {
            get {
                val users = userRepository.findAll()
                val userDtos = users.map(UserResponseDTO::from)
                call.respond(HttpStatusCode.OK, userDtos)
            }

            delete("/me") {
                val user = call.getAuthenticatedUser()

                if (user == null) {
                    call.respond(HttpStatusCode.Unauthorized, "User does not exist")
                    return@delete
                }

                userRepository.deleteById(user.id)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}
