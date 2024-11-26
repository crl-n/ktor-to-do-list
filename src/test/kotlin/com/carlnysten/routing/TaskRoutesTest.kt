package com.carlnysten.routing

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.enum.TaskPriority
import com.carlnysten.models.domain.Task
import com.carlnysten.models.domain.User
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
        var addedUser: User? = null

        application {
            configureTestApplication()

            // Add test user to database
            val userRepository by inject<UserRepository>()
            addedUser = userRepository.add(CreateUserDTO(
                "user",
                "pw"
            ))

            // Pre-condition: Task to be added should not be present in database
            val taskRepository by inject<TaskRepository>()
            val task = taskRepository.findByTaskId(1)
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
        // Post-condition: Added task should be present in database with correct values
        val taskRepository by inject<TaskRepository>()
        val task = taskRepository.findByTaskId(1)
        assertNotNull(task, "Task should not be null after adding task")
        assertEquals(addedUser?.id, task.userId, "Task should add task to authenticated user")
        assertEquals(TaskPriority.Normal, task.priority, "By default task priority should be Normal")
    }

    @Test
    fun `add task endpoint should assign priority from request if included`() = testApplication {
        application {
            configureTestApplication()

            // Add test user to database
            val userRepository by inject<UserRepository>()
            userRepository.add(CreateUserDTO(
                "user",
                "pw"
            ))
        }

        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            basicAuth("user", "pw")
            setBody(
                """
                    {
                        "name": "Test task",
                        "description": "A test task",
                        "priority": "high"
                    }
                """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.Created, response.status)
        // Added task should have the priority set to it in the request
        val taskRepository by inject<TaskRepository>()
        val task = taskRepository.findByTaskId(1)
        assertNotNull(task)
        assertEquals(TaskPriority.High, task.priority, "Task should get priority from request")
    }

    @Test
    fun `add task endpoint should return 400 Bad Request for task request without name`() = testApplication {
        application {
            configureTestApplication()

            // Add test user to database
            val userRepository by inject<UserRepository>()
            userRepository.add(CreateUserDTO(
                "user",
                "pw"
            ))
        }

        val response = client.post("/tasks") {
            contentType(ContentType.Application.Json)
            basicAuth("user", "pw")
            setBody(
                """
                    {
                        "description": "A test task"
                    }
                """.trimIndent()
            )
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        // Task should not be added to db for bad request
        val taskRepository by inject<TaskRepository>()
        val tasks = taskRepository.findAllByUserId(1)
        assertEquals(listOf<Task>(), tasks)
    }
}