package com.carlnysten.repositories

import com.carlnysten.models.dao.UserDAO
import com.carlnysten.models.domain.User
import com.carlnysten.models.dto.CreateUserDTO
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Base64

class UserRepository {
    fun findAll(): List<User> {
        return transaction {
            val users = UserDAO.all().map(User::from)
            return@transaction users
        }
    }

    fun add(dto: CreateUserDTO): User {
        val dao = transaction {
            UserDAO.new {
                username = dto.username
                passwordEncoded = Base64.getEncoder()
                    .encodeToString(dto.password.toByteArray())
            }
        }
        return User.from(dao)
    }
}
