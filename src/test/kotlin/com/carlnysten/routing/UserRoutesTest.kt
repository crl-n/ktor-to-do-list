package com.carlnysten.routing

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.models.dto.CreateUserDTO
import com.carlnysten.models.dto.UserResponseDTO
import com.carlnysten.plugins.*
import com.carlnysten.repositories.UserRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.client.plugins.contentnegotiation.*
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

    private fun Application.configureTestApplication() {
        val dbConfig = DatabaseConfig(
            jdbcUrl = postgresContainer.jdbcUrl,
            user = postgresContainer.username,
            password = postgresContainer.password
        )
        configureKoin()
        configureSecurity()
        configureSerialization()
        routing {
            addUserRoutes()
        }
        configureDatabase(dbConfig)
        runFlyway(dbConfig)
    }

    @Test
    fun `post user endpoint adds a new user to Postgres with correct values`() = testApplication {
        application {
            configureTestApplication()
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

    @Test
    fun `get users endpoint returns all users from db`() = testApplication {
        application {
            configureTestApplication()
            userRepository.add(CreateUserDTO("user1", "pw"))
            userRepository.add(CreateUserDTO("user2", "pw"))
            userRepository.add(CreateUserDTO("user3", "pw"))
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/users") {
            contentType(ContentType.Application.Json)
            basicAuth("user1", "pw")
        }
        val responseData: List<UserResponseDTO> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(3, responseData.size)
    }
}