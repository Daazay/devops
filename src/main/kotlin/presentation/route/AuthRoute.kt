package org.daazay.presentation.route

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import org.daazay.presentation.controller.AuthController
import org.daazay.presentation.request.AuthLoginRequest
import org.daazay.presentation.request.AuthSignupRequest
import org.daazay.utils.Result
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

fun Route.configureAuthRoute(
    controller: AuthController = getKoin().get<AuthController>(),
) {
    route("/auth") {
        post("/signup") {
            try {
                val request = call.receiveNullable<AuthSignupRequest>()
                    ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid request"))
                when(val result = controller.signup(request)) {
                    is Result.Error -> {
                        return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                    }
                    is Result.Success -> {
                        call.sessions.set(result.data!!.refreshToken)
                        return@post call.respond(HttpStatusCode.OK, result.data)
                    }
                }
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
            }
        }

        post("/login") {
            try {
                val request = call.receiveNullable<AuthLoginRequest>()
                    ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid request"))
                when(val result = controller.login(request)) {
                    is Result.Error -> {
                        return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                    }
                    is Result.Success -> {
                        call.sessions.set(result.data!!.refreshToken)
                        return@post call.respond(HttpStatusCode.OK, result.data)
                    }
                }
            } catch (e: Exception) {
                return@post call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
            }
        }

        authenticate("auth-user") {
            get("/logout") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                        ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to receive jwt principal."))
                    val sessionId = principal.getClaim("session_id", UUID::class)
                        ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to get an sessionId from jwt principal."))
                    val userId = principal.getClaim("user_id", UUID::class)
                        ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to get an userId from jwt principal."))
                    when(val result = controller.logout(sessionId, userId)) {
                        is Result.Error -> {
                            return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                        }
                        is Result.Success -> {
                            call.sessions.set("")
                            return@get call.respond(HttpStatusCode.OK)
                        }
                    }
                } catch (e: Exception) {
                    return@get call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
                }
            }
        }

        authenticate("auth-refresh") {
            get("/refresh") {
                try {
                    val principal = call.principal<JWTPrincipal>()
                        ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to receive jwt principal."))
                    val sessionId = principal.getClaim("session_id", UUID::class)
                        ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to get an sessionId from jwt principal."))
                    val userId = principal.getClaim("user_id", UUID::class)
                        ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Failed to get an userId from jwt principal."))
                    when(val result = controller.refreshToken(sessionId, userId)) {
                        is Result.Error -> {
                            return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                        }
                        is Result.Success -> {
                            call.sessions.set(result.data!!.refreshToken)
                            return@get call.respond(HttpStatusCode.OK, result.data)
                        }
                    }
                } catch (e: Exception) {
                    return@get call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
                }
            }
        }
    }
}