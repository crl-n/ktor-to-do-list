package com.carlnysten.models.dto

import com.carlnysten.enum.TaskPriority
import com.carlnysten.enum.TaskStatus
import kotlinx.serialization.Serializable

@Serializable
data class PatchTaskDTO(
    val name: String? = null,
    val description: String? = null,
    val priority: TaskPriority? = null,
    val status: TaskStatus? = null,
)
