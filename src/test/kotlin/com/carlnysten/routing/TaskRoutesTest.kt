package com.carlnysten.routing

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.models.dto.CreateUserDTO
import com.carlnysten.plugins.*
import com.carlnysten.repositories.TaskRepository
import com.carlnysten.repositories.UserRepository
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.koin.ktor.ext.inject
import org.koin.test.KoinTest
import org.koin.test.inject
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TaskRoutesTest : KoinTest {

    @Container
    private val postgresContainer = PostgreSQLContainer<Nothing>("postgres:17.1")
        .apply {
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
            addTaskRoutes()
        }
        configureDatabase(dbConfig)
        runFlyway(dbConfig)
    }

    @Test
    fun `add task endpoint should add task correctly`() = testApplication {
        application {
            configureTestApplication()

            // Add test user to database
            val userRepository by inject<UserRepository>()
            userRepository.add(CreateUserDTO(
                "user",
                "pw"
            ))

            // Pre-condition: Added user should not be present in database
            val taskRepository by inject<TaskRepository>()
            val task = taskRepository.findByUserId(1)
            assertNull(task)
        }

        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            basicAuth("user", "pw")
            setBody(
                """
                    {
                        "name": "Test task",
                        "description": "A test task"
                    }
                """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.Created, response.status)
        // Post-condition: Added user should be present in database
        val taskRepository by inject<TaskRepository>()
        val task = taskRepository.findByUserId(1)
        assertNotNull(task)
    }
}