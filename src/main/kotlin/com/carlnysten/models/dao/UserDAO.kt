package com.carlnysten.models.dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable: IntIdTable("app.users") {
    val username = varchar("username", 256)
    val passwordEncoded = text("password_encoded")
}

class UserDAO(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UserTable)

    var username by UserTable.username
    var passwordEncoded by UserTable.passwordEncoded
}
