package org.daazay.config

import org.daazay.utils.requireEnv

data class JWTConfig(
    val issuer: String = DEFAULT_ISSUER,
    val audience: String = DEFAULT_AUDIENCE,
    val realm: String = DEFAULT_REALM,
    val accessSecret: String,
    val accessExpiresIn: Long = DEFAULT_ACCESS_EXPIRES_IN,
    val refreshSecret: String,
    val refreshExpiresIn: Long = DEFAULT_REFRESH_EXPIRES_IN,
) {
    companion object {
        private const val ENV_PREFIX = "JWT_"

        const val DEFAULT_ISSUER = "issuer"
        const val DEFAULT_AUDIENCE = "audience"
        const val DEFAULT_REALM = "realm"
        const val DEFAULT_ACCESS_EXPIRES_IN = 15L * 60L * 1000L // 15 min
        const val DEFAULT_REFRESH_EXPIRES_IN = 7L * 24L * 60L * 60L * 1000L // 7 day

        fun fromEnv() = JWTConfig(
            issuer = requireEnv("${ENV_PREFIX}ISSUER") { DEFAULT_ISSUER },
            accessSecret = requireEnv("${ENV_PREFIX}ACCESS_SECRET"),
            accessExpiresIn = requireEnv("${ENV_PREFIX}ACCESS_EXPIRES_IN") { DEFAULT_ACCESS_EXPIRES_IN },
            refreshSecret = requireEnv("${ENV_PREFIX}REFRESH_SECRET"),
            refreshExpiresIn = requireEnv("${ENV_PREFIX}REFRESH_EXPIRES_IN") { DEFAULT_REFRESH_EXPIRES_IN },
        )
    }
}