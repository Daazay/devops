package org.daazay.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import org.daazay.config.JWTConfig
import org.koin.java.KoinJavaComponent.getKoin

fun Application.configureSessions(
    jwtConfig: JWTConfig = getKoin().get<JWTConfig>(),
) {
    install(Sessions) {
        cookie<String>("refresh_token") {
            cookie.path = "/api/auth/refresh"
            cookie.maxAgeInSeconds = jwtConfig.refreshExpiresIn
            cookie.httpOnly = true
        }
    }
}