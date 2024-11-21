package com.carlnysten.repositories

import com.carlnysten.models.dao.UserDAO
import com.carlnysten.models.dao.UserTable
import com.carlnysten.models.domain.User
import com.carlnysten.models.dto.CreateUserDTO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.Base64

class UserRepository {
    fun findAll(): List<User> {
        return transaction {
            UserDAO.all().map(User::from)
        }
    }

    fun findByUsername(username: String): User? {
        return transaction {
            UserDAO.find { UserTable.username eq username }
                .firstOrNull()
                ?.let(User::from)
        }
    }

    fun validateCredentials(username: String, password: String): Boolean {
        return transaction {
            val passwordEncoded = Base64.getEncoder()
                .encodeToString(password.toByteArray())

            return@transaction UserDAO.find {
                (UserTable.username eq username) and
                (UserTable.passwordEncoded eq passwordEncoded)
            }
                .firstOrNull()
                ?.let { true } ?: false
        }
    }

    fun add(dto: CreateUserDTO): User {
        return transaction {
            UserDAO.new {
                username = dto.username
                passwordEncoded = Base64.getEncoder()
                    .encodeToString(dto.password.toByteArray())
            }.let(User::from)
        }
    }
}
