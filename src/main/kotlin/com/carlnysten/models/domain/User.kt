package com.carlnysten.models.domain

import com.carlnysten.models.dao.UserDAO
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
    val passwordEncoded: String,
) {
    companion object {
        fun from(dao: UserDAO): User {
            return User(
                dao.id.value,
                dao.username,
                dao.passwordEncoded,
            )
        }
    }
}
