package com.carlnysten.plugins

import com.carlnysten.repositories.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*

val userRepository = UserRepository()

fun Application.configureSecurity() {
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
