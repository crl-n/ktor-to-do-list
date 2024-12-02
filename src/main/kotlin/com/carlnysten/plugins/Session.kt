package com.carlnysten.plugins

import com.carlnysten.models.dto.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSessions(
    sessionStorage: SessionStorage = SessionStorageMemory()
) {
    install(Sessions) {
        cookie<UserSession>("user_session", sessionStorage) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 120
        }
    }
}
