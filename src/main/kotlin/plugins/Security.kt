package org.daazay.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.daazay.config.JWTConfig
import org.daazay.domain.model.UserRole
import org.koin.java.KoinJavaComponent.getKoin

fun Application.configureSecurity(
    config: JWTConfig = getKoin().get<JWTConfig>(),
) {
    val verifier = JWT.require(Algorithm.HMAC256(config.accessSecret))
        .withSubject("access")
        .withAudience(config.audience)
        .withIssuer(config.issuer)
        .build()

    authentication {
        jwt("auth-user") {
            realm = config.realm
            verifier(verifier)
            validate { credential -> JWTPrincipal(credential.payload) }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token is not valid or has expired"))
            }
        }
        jwt("auth-admin") {
            realm = config.realm
            verifier(verifier)
            validate { credential ->
                val role = credential.payload.getClaim("role").asString()
                if (role == UserRole.ADMIN.name) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token is not valid or has expired"))
            }
        }
        jwt("auth-refresh") {
            verifier(JWT.require(Algorithm.HMAC256(config.refreshSecret))
                    .withSubject("refresh")
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build())

            authHeader { call ->
                val oldHeader = call.request.parseAuthorizationHeader()
                val jwt = call.sessions.get<String>()

                if (!jwt.isNullOrBlank()) {
                    HttpAuthHeader.Single(oldHeader?.authScheme ?: "Bearer", jwt)
                } else {
                    oldHeader
                }
            }
            validate { credential -> JWTPrincipal(credential.payload) }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token is not valid or has expired"))
            }
        }
    }
}