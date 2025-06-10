package org.daazay.plugins

import io.ktor.server.application.*
import io.micrometer.core.instrument.Counter
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.daazay.config.DBConfig
import org.daazay.config.JWTConfig
import org.daazay.data.repository.auth.SessionRepositoryImpl
import org.daazay.data.repository.auth.UserRepositoryImpl
import org.daazay.data.repository.booking.BookingRepositoryImpl
import org.daazay.data.repository.booking.BuildingRepositoryImpl
import org.daazay.data.repository.booking.RoomRepositoryImpl
import org.daazay.domain.repository.auth.SessionRepository
import org.daazay.domain.repository.auth.UserRepository
import org.daazay.domain.repository.building.BookingRepository
import org.daazay.domain.repository.building.BuildingRepository
import org.daazay.domain.repository.building.RoomRepository
import org.daazay.domain.service.auth.AuthService
import org.daazay.domain.service.auth.HashingService
import org.daazay.domain.service.auth.TokenService
import org.daazay.domain.service.building.BookingService
import org.daazay.domain.service.building.BuildingService
import org.daazay.domain.service.building.RoomService
import org.daazay.presentation.controller.auth.AuthController
import org.daazay.presentation.controller.booking.BookingController
import org.daazay.presentation.controller.booking.BuildingController
import org.daazay.presentation.controller.booking.RoomController
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDI() {
    install(Koin) {
        modules(module {
            single { DBConfig.fromEnv() } withOptions {
                createdAtStart()
            }
            single { JWTConfig.fromEnv() } withOptions {
                createdAtStart()
            }
            single {
                PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
            }  withOptions {
                createdAtStart()
            }

            single<UserRepository> { UserRepositoryImpl() }
            single<SessionRepository> { SessionRepositoryImpl() }

            single<BuildingRepository> { BuildingRepositoryImpl() }
            single<RoomRepository> { RoomRepositoryImpl() }
            single<BookingRepository> { BookingRepositoryImpl() }

            single { TokenService(get()) }
            single { HashingService() }
            single { AuthService(get(), get(), get(), get()) }

            single { BuildingService(get()) }
            single { RoomService(get()) }
            single { BookingService(get()) }

            single { AuthController(get()) }

            single { BuildingController(get()) }
            single { RoomController(get()) }
            single { BookingController(get()) }
        })
    }
}