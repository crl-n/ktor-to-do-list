package com.carlnysten.repositories

import com.carlnysten.enum.TaskPriority
import com.carlnysten.enum.TaskStatus
import com.carlnysten.models.dao.TaskDAO
import com.carlnysten.models.dao.TaskTable
import com.carlnysten.models.domain.Task
import com.carlnysten.models.dto.CreateTaskDTO
import com.carlnysten.models.dto.PatchTaskDTO
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class TaskRepository {
    fun findByTaskId(taskId: Int): Task? {
        return transaction {
            TaskDAO.findById(taskId)?.let(Task::from)
        }
    }

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
                this.priority = dto.priority ?: TaskPriority.Normal
            }.let(Task::from)
        }
    }

    fun updateByTaskId(taskId: Int, patchTaskDto: PatchTaskDTO): Task? {
        return transaction {
            TaskDAO.findByIdAndUpdate(taskId) {
                it.name = patchTaskDto.name ?: it.name
                it.description = patchTaskDto.description ?: it.description
                it.priority = patchTaskDto.priority ?: it.priority
                it.status = patchTaskDto.status ?: it.status
            }?.let(Task::from)
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
