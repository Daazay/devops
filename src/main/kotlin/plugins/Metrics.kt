package org.daazay.plugins

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.java.KoinJavaComponent.getKoin

fun Application.configureMetrics(
    myRegistry:  PrometheusMeterRegistry = getKoin().get<PrometheusMeterRegistry>(),
) {
    install(MicrometerMetrics) {
        registry = myRegistry
    }
}