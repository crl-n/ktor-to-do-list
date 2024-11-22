package com.carlnysten.plugins

import com.carlnysten.repositories.UserRepository
import com.carlnysten.repositories.TaskRepository
import io.ktor.server.application.*
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {
    singleOf(::UserRepository)
    singleOf(::TaskRepository)
}

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}
