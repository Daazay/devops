package org.daazay

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.core.instrument.Counter
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.exporter.common.TextFormat
import org.daazay.plugins.*
import org.daazay.presentation.route.auth.configureAuthRoute
import org.daazay.presentation.route.booking.configureBookingRoute
import org.daazay.presentation.route.booking.configureBuildingRoute
import org.daazay.presentation.route.booking.configureRoomRoute
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.java.KoinJavaComponent.getKoin
import java.util.concurrent.ConcurrentHashMap

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureDI()
    configureMetrics()
    configureSerialization()
    configureDatabase()
    configureCORS()
    configureSecurity()
    configureSessions()

    val registry = getKoin().get<PrometheusMeterRegistry>()

    val requestsTotalCounter = Counter.builder("requests_total")
        .description("Total received requests")
        .register(registry)

    val counters = ConcurrentHashMap<String, Counter>()

    // Function to get or create a counter for the given route
    fun getCounterForRoute(route: String): Counter {
        return counters.computeIfAbsent(route) {
            Counter.builder("requests_by_route_total")
                .description("Total HTTP requests per route")
                .tag("route", it)
                .register(registry)
        }
    }

    intercept(ApplicationCallPipeline.Monitoring) {
        val route = call.request.path()
        getCounterForRoute(route).increment()
        requestsTotalCounter.increment()
    }

    routing {
        route("/api") {
            configureAuthRoute()

            configureBuildingRoute()
            configureRoomRoute()
            configureBookingRoute()
        }
        route("/") {
            get("/metrics") {
                call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004)) {
                    registry.scrape(this)
                }
            }

            get("/health") {
                return@get call.respondText("OK")
            }
        }
    }
}