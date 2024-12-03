package com.carlnysten.config

data class DatabaseConfig(
    val jdbcUrl: String,
    val user: String,
    val password: String
)
