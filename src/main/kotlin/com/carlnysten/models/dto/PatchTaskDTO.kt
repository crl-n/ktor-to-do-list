package com.carlnysten.models.dto

import com.carlnysten.enum.TaskPriority
import kotlinx.serialization.Serializable

@Serializable
data class PatchTaskDTO(
    val name: String? = null,
    val description: String? = null,
    val priority: TaskPriority? = null,
)
