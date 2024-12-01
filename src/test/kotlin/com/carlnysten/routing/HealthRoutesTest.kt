package com.carlnysten.routing

import com.carlnysten.config.DatabaseConfig
import com.carlnysten.plugins.*
import com.carlnysten.testcontainers.getPostgresContainer
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.testcontainers.junit.jupiter.Container
import kotlin.test.Test
import kotlin.test.assertEquals

class HealthRoutesTest {

    @Container
    private val postgresContainer = getPostgresContainer()

    @Test
    fun `live endpoint returns 200 OK`() = testApplication {
        application {
            routing {
                addHealthRoutes()
            }
        }

        val response = client.get("/health/live")
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `ready endpoint returns 200 OK`() = testApplication {
        application {
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
                addHealthRoutes()
            }
            configureDatabase(dbConfig)
            runFlyway(dbConfig)
        }

        val response = client.get("/health/ready")
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
