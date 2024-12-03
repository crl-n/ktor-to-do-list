package com.carlnysten.plugins

import com.carlnysten.models.dto.UserSession
import io.ktor.server.application.*
import io.ktor.server.sessions.*

fun Application.configureSessions(
    sessionStorage: SessionStorage = SessionStorageMemory(),
    sessionDurationSeconds: Long = 120,
) {
    install(Sessions) {
        cookie<UserSession>("user_session", sessionStorage) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = sessionDurationSeconds
        }
    }
}
