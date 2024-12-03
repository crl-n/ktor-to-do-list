package com.carlnysten.repositories

import com.carlnysten.models.dao.TaskCollectionDAO
import com.carlnysten.models.dao.TaskCollectionTable
import com.carlnysten.models.domain.TaskCollection
import com.carlnysten.models.dto.CreateTaskCollectionDTO
import org.jetbrains.exposed.sql.transactions.transaction

class TaskCollectionRepository {
    fun findAllByUserId(userId: Int): List<TaskCollection> {
        return transaction {
            TaskCollectionDAO.find { TaskCollectionTable.userId eq userId }
                .map(TaskCollection::from)
        }
    }

    fun addForUserId(userId: Int, dto: CreateTaskCollectionDTO): TaskCollection {
        return transaction {
            TaskCollectionDAO.new {
                name = dto.name
                description = dto.description
                this.userId = userId
            }.let(TaskCollection::from)
        }
    }
}
