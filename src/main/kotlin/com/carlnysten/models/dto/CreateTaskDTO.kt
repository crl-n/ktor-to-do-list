package com.carlnysten.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskDTO(
    val name: String,
    val description: String? = null,
)
