package com.carlnysten.models.dto

import com.carlnysten.models.domain.Task
import kotlinx.serialization.Serializable

@Serializable
data class TaskResponseDTO(
    val id: Int,
    val name: String,
    val description: String?,
    val collectionId: Int?
) {
    companion object {
        fun from(task: Task) = TaskResponseDTO(
            task.id,
            task.name,
            task.description,
            task.collectionId
        )
    }
}
