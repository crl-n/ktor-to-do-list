package com.carlnysten.routing

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.models.dto.CreateUserDTO
import com.carlnysten.models.dto.UserResponseDTO
import com.carlnysten.plugins.*
import com.carlnysten.repositories.UserRepository
import com.carlnysten.testcontainers.getPostgresContainer
import com.carlnysten.testcontainers.reset
import com.carlnysten.utils.createClientWithJsonNegotiation
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.testcontainers.junit.jupiter.Container
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.Base64

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRoutesTest : KoinTest {

    private val userRepository: UserRepository by inject()

    @Container
    private val postgresContainer = getPostgresContainer()

    @AfterEach
    fun afterEach(): Unit {
        postgresContainer.reset()
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
        configureSessions()
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

        val client = createClientWithJsonNegotiation()

        val response = client.get("/users") {
            contentType(ContentType.Application.Json)
            basicAuth("user1", "pw")
        }
        val responseData: List<UserResponseDTO> = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(3, responseData.size)
    }

    @Test
    fun `get users endpoint does not return users to unauthenticated user`() = testApplication {
        application {
            configureTestApplication()
            userRepository.add(CreateUserDTO("user1", "pw"))
            userRepository.add(CreateUserDTO("user2", "pw"))
            userRepository.add(CreateUserDTO("user3", "pw"))
        }

        val client = createClientWithJsonNegotiation()

        val response = client.get("/users") {
            contentType(ContentType.Application.Json)
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `get me endpoint returns authenticated user`() = testApplication {
        application {
            configureTestApplication()
            userRepository.add(CreateUserDTO("user", "pw"))
        }

        val client = createClientWithJsonNegotiation()

        val response = client.get("/users/me") {
            basicAuth("user", "pw")
        }
        val responseData: UserResponseDTO = response.body()

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("user", responseData.username)
    }

    @Test
    fun `delete me endpoint deletes authenticated user`() = testApplication {
        application {
            configureTestApplication()
            userRepository.add(CreateUserDTO("user", "pw"))

            // Precondition: There should be one user before removal
            val allUsersBefore = userRepository.findAll()
            assertEquals(1, allUsersBefore.size)
        }

        val client = createClientWithJsonNegotiation()

        val response = client.delete("/users/me") {
            basicAuth("user", "pw")
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
        // Post-condition: Since there was 1 user before deletion, there should now be 0
        val allUsers = userRepository.findAll()
        assertEquals(0, allUsers.size)
    }

    @Test
    fun `delete me endpoint does not accept unauthenticated requests`() = testApplication {
        application {
            configureTestApplication()
            userRepository.add(CreateUserDTO("user", "pw"))

            // Precondition: There should be one user before removal
            val allUsersBefore = userRepository.findAll()
            assertEquals(1, allUsersBefore.size)
        }

        val client = createClientWithJsonNegotiation()

        val response = client.delete("/users/me")

        assertEquals(HttpStatusCode.Unauthorized, response.status)
        // Post-condition: There should still be 1 user after request
        val allUsers = userRepository.findAll()
        assertEquals(1, allUsers.size)
    }
}
