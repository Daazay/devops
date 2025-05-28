package org.daazay

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.daazay.plugins.*
import org.daazay.presentation.route.auth.configureAuthRoute
import org.daazay.presentation.route.booking.configureBookingRoute
import org.daazay.presentation.route.booking.configureBuildingRoute
import org.daazay.presentation.route.booking.configureRoomRoute

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

            configureBuildingRoute()
            configureRoomRoute()
            configureBookingRoute()

            get("/health") {
                return@get call.respondText("OK")
            }
        }
    }
}