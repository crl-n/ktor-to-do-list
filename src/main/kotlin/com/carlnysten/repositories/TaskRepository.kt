package com.carlnysten.repositories

import com.carlnysten.models.dao.TaskDAO
import com.carlnysten.models.dao.TaskTable
import com.carlnysten.models.domain.Task
import com.carlnysten.models.dto.CreateTaskDTO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TaskRepository {
    fun findAllByUserId(userId: Int): List<Task> {
        return transaction {
            TaskDAO.find { TaskTable.userId eq userId }
                .map(Task::from)
        }
    }

    fun addForUserId(dto: CreateTaskDTO, userId: Int): Task {
        return transaction {
            TaskDAO.new {
                name = dto.name
                description = dto.description
                this.userId = userId
                this.collectionId = dto.collectionId
            }.let(Task::from)
        }
    }

    fun assignTaskToCollection(userId: Int, collectionId: Int, taskId: Int) {
        return transaction {
            TaskTable.update({
                (TaskTable.id eq taskId) and (TaskTable.userId eq userId)
            }) {
                it[TaskTable.collectionId] = collectionId
            }
        }
    }
}
