package com.carlnysten.models.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TaskTable: IntIdTable("app.tasks") {
    val name = varchar("name", 256)
    val description = varchar("description", 2048).nullable()
    val userId = integer("user_id")
    val collectionId = integer("task_collection_id").nullable()
}

class TaskDAO(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<TaskDAO>(TaskTable)

    var name by TaskTable.name
    var description by TaskTable.description
    var userId by TaskTable.userId
    var collectionId by TaskTable.collectionId
}
