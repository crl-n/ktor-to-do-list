package com.carlnysten.plugins

import com.carlnysten.models.domain.User
import com.carlnysten.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val userRepository by inject<UserRepository>()

    authentication {
        basic(name = "auth-basic") {
            realm = "Todolist API"
            validate { credentials ->
                val isValid = userRepository.validateCredentials(
                    credentials.name,
                    credentials.password
                )

                if (isValid) {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }
}

fun ApplicationCall.getAuthenticatedUser(): User? {
    val userRepository by application.inject<UserRepository>()
    val principal = principal<UserIdPrincipal>() ?: return null
    val username = principal.name
    return userRepository.findByUsername(username)
}
