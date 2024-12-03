package com.carlnysten.models.dto

import com.carlnysten.enum.TaskPriority
import kotlinx.serialization.Serializable

@Serializable
data class CreateTaskDTO(
    val name: String,
    val description: String? = null,
    val collectionId: Int? = null,
    val priority: TaskPriority? = null,
)
