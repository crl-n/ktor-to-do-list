package com.carlnysten.plugins

import com.carlnysten.exceptions.AuthenticationException
import com.carlnysten.models.domain.User
import com.carlnysten.models.dto.UserSession
import com.carlnysten.repositories.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
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

        session<UserSession>("auth-session") {
            validate { session ->
                if (validateUsername(session.username)) {
                    session
                } else {
                    null
                }
            }

            challenge {
                call.respondRedirect("/login")
            }
        }
    }
}

fun ApplicationCall.validateUsername(username: String): Boolean {
    val userRepository by inject<UserRepository>()

    return userRepository.findByUsername(username) != null
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
    val user: User?

    val session = sessions.get<UserSession>()

    if (session != null) {
        user = userRepository.findByUsername(session.username)
    } else {
        val principal = principal<UserIdPrincipal>()
            ?: throw AuthenticationException("Null principal")

        user = userRepository.findByUsername(principal.name)
    }

    if (user == null)
        throw AuthenticationException("Authenticated user not found")

    return user
}
