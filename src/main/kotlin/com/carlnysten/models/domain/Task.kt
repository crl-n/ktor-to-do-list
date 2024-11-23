package com.carlnysten.models.domain

import com.carlnysten.models.dao.TaskDAO
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Int,
    val name: String,
    val description: String?,
    val userId: Int,
    val collectionId: Int?
) {
    companion object {
        fun from(dao: TaskDAO): Task {
            return Task(
                dao.id.value,
                dao.name,
                dao.description,
                dao.userId,
                dao.collectionId
            )
        }
    }
}
