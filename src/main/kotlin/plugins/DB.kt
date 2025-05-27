package org.daazay.plugins

import io.ktor.server.application.*
import org.daazay.config.DBConfig
import org.jetbrains.exposed.sql.Database
import org.koin.java.KoinJavaComponent.getKoin

fun Application.configureDatabase(
    config: DBConfig = getKoin().get<DBConfig>(),
) {
    Database.connect(
        driver = config.driver,
        url = config.url,
        user = config.username,
        password = config.password
    )
}