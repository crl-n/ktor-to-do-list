package com.carlnysten.routing

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.plugins.*
import com.carlnysten.repositories.UserRepository
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.Base64

class UserRoutesTest : KoinTest {

    private val userRepository: UserRepository by inject()

    @Container
    private val postgresContainer = PostgreSQLContainer<Nothing>("postgres:17.1").apply {
        withDatabaseName("todolist")
        withUsername("postgres")
        withPassword("postgres")
        start()
    }

    @Test
    fun `post user endpoint adds a new user to Postgres with correct values`() = testApplication {
        val dbConfig = DatabaseConfig(
            jdbcUrl = postgresContainer.jdbcUrl,
            user = postgresContainer.username,
            password = postgresContainer.password
        )
        application {
            configureKoin()
            configureSecurity()
            configureSerialization()
            routing {
                addUserRoutes()
            }
            configureDatabase(dbConfig)
            runFlyway(dbConfig)
        }

        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody(
                """
                    {
                        "username": "user",
                        "password": "pw"
                    }
                """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.Created, response.status)

        val user = userRepository.findByUsername("user")
        assertNotNull(user)
        assertEquals(Base64.getEncoder().encodeToString("pw".toByteArray()), user.passwordEncoded)
    }
}
