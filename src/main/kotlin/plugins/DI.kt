package org.daazay.plugins

import io.ktor.server.application.*
import org.daazay.config.DBConfig
import org.daazay.config.JWTConfig
import org.daazay.data.repository.SessionRepositoryImpl
import org.daazay.data.repository.UserRepositoryImpl
import org.daazay.domain.service.AuthService
import org.daazay.domain.service.HashingService
import org.daazay.domain.service.TokenService
import org.daazay.presentation.controller.AuthController
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

fun Application.configureDI() {
    startKoin {
        modules(module {
            single { DBConfig.fromEnv() }
            single { JWTConfig.fromEnv() }

            single { UserRepositoryImpl() }
            single { SessionRepositoryImpl() }

            single { TokenService(get()) }
            single { HashingService() }
            single { AuthService(get(), get(), get(), get()) }
            single { AuthController(get()) }
        })
    }
}