package com.carlnysten.models.session

import io.ktor.server.sessions.*
import io.lettuce.core.api.StatefulRedisConnection

class RedisSessionStorage(
    private val connection: StatefulRedisConnection<String, String>
) : SessionStorage {
    private val syncCommands = connection.sync()

    override suspend fun invalidate(id: String) {
        syncCommands.del(id)
    }

    override suspend fun read(id: String): String {
        return syncCommands.get(id)
            ?: throw NoSuchElementException("Session with ID $id not found")
    }

    override suspend fun write(id: String, value: String) {
        println("$id $value")
        syncCommands.set(id, value)
    }
}
