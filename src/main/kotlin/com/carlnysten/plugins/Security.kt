package com.carlnysten.plugins

import com.carlnysten.exceptions.AuthenticationException
import com.carlnysten.models.domain.User
import com.carlnysten.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    authentication {
        basic(name = "auth-basic") {
            realm = "Todolist API"
            validate(ApplicationCall::validateCredentials)
        }

        form("auth-form") {
            userParamName="username"
            passwordParamName="password"

            validate(ApplicationCall::validateCredentials)
            challenge {
                call.respond(HttpStatusCode.Unauthorized, "Credentials are not valid")
            }
        }
    }
}

fun ApplicationCall.validateCredentials(credentials: UserPasswordCredential): Any? {
    val userRepository by inject<UserRepository>()

    val isValid = userRepository.validateCredentials(
        credentials.name,
        credentials.password
    )

    return if (isValid) {
        UserIdPrincipal(credentials.name)
    } else {
        null
    }
}

fun ApplicationCall.getAuthenticatedUser(): User {
    val userRepository by application.inject<UserRepository>()
    val principal = principal<UserIdPrincipal>()
        ?: throw AuthenticationException("Null principal")

    val user = userRepository.findByUsername(principal.name)
        ?: throw AuthenticationException("Authenticated user ${principal.name} not found")

    return user
}
