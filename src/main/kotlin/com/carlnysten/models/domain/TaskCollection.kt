package com.carlnysten.models.domain

import com.carlnysten.models.dao.TaskCollectionDAO

data class TaskCollection(
    val id: Int,
    val name: String,
    val description: String?,
    val userId: Int
) {
    companion object {
        fun from(dao: TaskCollectionDAO): TaskCollection {
            return TaskCollection(
                dao.id.value,
                dao.name,
                dao.description,
                dao.userId
            )
        }
    }
}
