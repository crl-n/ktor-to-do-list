package com.carlnysten.models.dto

import com.carlnysten.models.domain.User
import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDTO(
    val id: Int,
    val username: String,
) {
    companion object {
        fun from(user: User) = UserResponseDTO(user.id, user.username)
    }
}
