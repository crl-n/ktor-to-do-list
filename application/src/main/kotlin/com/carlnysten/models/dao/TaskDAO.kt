package com.carlnysten.models.dao

import com.carlnysten.enum.TaskPriority
import com.carlnysten.enum.TaskStatus
import com.carlnysten.util.PGEnum
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TaskTable: IntIdTable("app.tasks") {
    val name = varchar("name", 256)
    val description = varchar("description", 2048).nullable()
    val userId = integer("user_id")
    val collectionId = integer("task_collection_id").nullable()
    val priority: Column<TaskPriority> = customEnumeration(
        name = "priority",
        sql ="task_priority",
        fromDb = { value -> TaskPriority.valueOf((value as String).replaceFirstChar { it.uppercase() }) },
        toDb = { PGEnum("task_priority", it) }
    ).default(TaskPriority.Normal)
    val status: Column<TaskStatus> = customEnumeration(
        name = "status",
        sql ="task_status",
        fromDb = { value -> TaskStatus.valueOf((value as String).replaceFirstChar { it.uppercase() }) },
        toDb = { PGEnum("task_status", it) }
    ).default(TaskStatus.Pending)
}

class TaskDAO(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TaskDAO>(TaskTable)

    var name by TaskTable.name
    var description by TaskTable.description
    var userId by TaskTable.userId
    var collectionId by TaskTable.collectionId
    var priority by TaskTable.priority
    var status by TaskTable.status
}
