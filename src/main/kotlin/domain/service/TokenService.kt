package org.daazay.domain.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.daazay.config.JWTConfig
import org.daazay.domain.model.UserRole
import java.util.UUID
import java.util.Date

class TokenService(
    private val config: JWTConfig,
) {
    fun generateAccessToken(sessionId: UUID, userId: UUID, email: String, role: UserRole): String {
        return JWT.create()
            .withSubject("access")
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("session_id", sessionId.toString())
            .withClaim("user_id", userId.toString())
            .withClaim("email", email)
            .withClaim("role", role.name)
            .withExpiresAt(Date(System.currentTimeMillis() + config.accessExpiresIn))
            .sign(Algorithm.HMAC256(config.accessSecret))
    }
    fun generateRefreshToken(sessionId: UUID, userId: UUID): String {
        return JWT.create()
            .withSubject("refresh")
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withClaim("user_id", userId.toString())
            .withClaim("session_id", sessionId.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + config.refreshExpiresIn))
            .sign(Algorithm.HMAC256(config.refreshSecret))
    }

    fun generateTokens(sessionId: UUID, userId: UUID, email: String, role: UserRole): Pair<String, String> {
        return Pair(generateAccessToken(sessionId, userId, email, role), generateRefreshToken(sessionId, userId))
    }
}