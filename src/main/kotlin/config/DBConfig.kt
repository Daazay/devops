package org.daazay.config

import org.daazay.utils.requireEnv

data class DBConfig(
    val url: String = DEFAULT_URL,
    val driver: String = DEFAULT_DRIVER,
    val username: String = DEFAULT_USERNAME,
    val password: String = DEFAULT_PASSWORD,
) {
    companion object {
        private const val ENV_PREFIX = "DB_"

        const val DEFAULT_URL = "jdbc:postgresql://localhost:5432/roombook"
        const val DEFAULT_DRIVER = "org.postgresql.Driver"
        const val DEFAULT_USERNAME = "postgres"
        const val DEFAULT_PASSWORD = "postgres"

        fun fromEnv():DBConfig {
            return if (System.getenv("${ENV_PREFIX}USE_H2").toBoolean()) {
                DBConfig(
                    url = "jdbc:h2:mem:roombook;DB_CLOSE_DELAY=-1",
                    driver = "org.h2.Driver",
                    username = "root",
                    password = ""
                )
            } else {
                DBConfig(
                    url = requireEnv("${ENV_PREFIX}URL") { DEFAULT_URL },
                    driver = requireEnv("${ENV_PREFIX}DRIVER") { DEFAULT_DRIVER },
                    username = requireEnv("${ENV_PREFIX}USERNAME") { DEFAULT_USERNAME },
                    password = requireEnv("${ENV_PREFIX}PASSWORD") { DEFAULT_PASSWORD },
                )
            }
        }
    }
}