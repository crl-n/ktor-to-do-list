package com.carlnysten.models.dto

import com.carlnysten.models.domain.TaskCollection
import kotlinx.serialization.Serializable

@Serializable
data class TaskCollectionResponseDTO(
    val id: Int,
    val name: String,
    val description: String?,
) {
    companion object {
        fun from(taskCollection: TaskCollection) = TaskCollectionResponseDTO(
            taskCollection.id,
            taskCollection.name,
            taskCollection.description,
        )
    }
}
