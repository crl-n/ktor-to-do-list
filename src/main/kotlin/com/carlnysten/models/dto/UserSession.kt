package com.carlnysten.models.dto

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(val userId: Int, val username: String) : Principal
