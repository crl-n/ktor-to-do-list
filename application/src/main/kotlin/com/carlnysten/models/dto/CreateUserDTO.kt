package com.carlnysten.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserDTO(
    val username: String,
    val password: String
)
