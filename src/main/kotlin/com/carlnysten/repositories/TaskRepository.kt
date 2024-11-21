package com.carlnysten.repositories

import com.carlnysten.models.dao.TaskDAO
import com.carlnysten.models.dao.TaskTable
import com.carlnysten.models.domain.Task
import com.carlnysten.models.dto.CreateTaskDTO
import org.jetbrains.exposed.sql.transactions.transaction

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
            }.let(Task::from)
        }
    }
}
