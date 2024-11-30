package com.carlnysten.models.domain

import com.carlnysten.enum.TaskPriority
import com.carlnysten.enum.TaskStatus
import com.carlnysten.models.dao.TaskDAO
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    val userId: Int,
    val collectionId: Int?,
    val priority: TaskPriority,
    val status: TaskStatus,
) {
    companion object {
        fun from(dao: TaskDAO): Task {
            return Task(
                dao.id.value,
                dao.name,
                dao.description,
                dao.userId,
                dao.collectionId,
                dao.priority,
                dao.status
            )
        }
    }
}
