package com.carlnysten.routing

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class HealthRoutesTest {

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
}
