package org.daazay

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.daazay.plugins.*
import org.daazay.presentation.route.configureAuthRoute

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureSerialization()
    configureDatabase()
    configureCORS()
    configureSecurity()
    configureSessions()

    routing {
        route("/api") {
            configureAuthRoute()
            get("/health") {
                return@get call.respondText("OK")
            }
        }
    }
}