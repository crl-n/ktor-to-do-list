package com.carlnysten.models.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TaskCollectionTable: IntIdTable("app.task_collections") {
    val name = varchar("name",256)
    val description = varchar("description",2048).nullable()
    val userId = integer("user_id")
}

class TaskCollectionDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TaskCollectionDAO>(TaskCollectionTable)

    var name by TaskCollectionTable.name
    var description by TaskCollectionTable.description
    var userId by TaskCollectionTable.userId
}
