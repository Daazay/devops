package org.daazay.domain.service.auth

import org.daazay.domain.model.auth.Auth
import org.daazay.domain.model.auth.SaltedHash
import org.daazay.domain.model.auth.UserCreate
import org.daazay.domain.model.auth.UserRole
import org.daazay.domain.repository.auth.SessionRepository
import org.daazay.domain.repository.auth.UserRepository
import org.daazay.presentation.request.auth.AuthLoginRequest
import org.daazay.presentation.request.auth.AuthSignupRequest
import java.util.UUID

class AuthService(
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository,
    private val tokenService: TokenService,
    private val hashingService: HashingService,
) {
    private fun validateUsername(username: String) {
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(username.length <= 64) { "Username cannot be more than 64 characters long" }
    }

    private fun validateEmail(email: String) {
        require(email.isNotBlank()) { "Email cannot be blank" }
        require(email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))) {
            "Email has invalid format"
        }
    }

    private fun validatePassword(password: String) {
        require(password.isNotBlank()) { "Password cannot be blank" }
    }

    suspend fun signup(auth: AuthSignupRequest): Auth {
        try {
            validateUsername(auth.username)
            validateEmail(auth.email)
            validatePassword(auth.password)

            val saltedHash = hashingService.generateSaltedHash(auth.password)
            val user = userRepository.add(
                UserCreate(
                    username = auth.username,
                    email = auth.email,
                    password = saltedHash.hash,
                    salt = saltedHash.salt,
                    role = UserRole.USER,
                )
            )

            val sessionId = UUID.randomUUID()
            val (accessToken, refreshToken) = tokenService.generateTokens(sessionId, user.id, user.email, user.role)

            sessionRepository.add(sessionId, user.id, refreshToken)

            return Auth(
                accessToken = accessToken,
                refreshToken = refreshToken,
                user = user
            )
        } catch (e: Exception) {
            throw Exception("Something went wrong while signup: ${e.localizedMessage}", e)
        }
    }

    suspend fun login(auth: AuthLoginRequest): Auth {
        try {
            validateEmail(auth.email)
            validatePassword(auth.password)

            val user = userRepository.findByEmail(auth.email)

            require(hashingService.verify(auth.password, SaltedHash(user.password, user.salt))) {
                "invalid password"
            }

            val sessionId = UUID.randomUUID()
            val (accessToken, refreshToken)  = tokenService.generateTokens(sessionId, user.id, user.email, user.role)

            sessionRepository.add(sessionId, user.id, refreshToken)

            return Auth(
                accessToken = accessToken,
                refreshToken = refreshToken,
                user = user
            )
        } catch (e: Exception) {
            throw Exception("Something went wrong while login: ${e.localizedMessage}", e)
        }
    }

    suspend fun logout(sessionId: UUID, userId: UUID) {
        try {
            sessionRepository.deleteByIdAndUserId(sessionId, userId)
        } catch (e: Exception) {
            throw Exception("Something went wrong while logout: ${e.localizedMessage}", e)
        }
    }

    suspend fun logoutEverywhere(userId: UUID): Int {
        try {
            return sessionRepository.deleteByUserId(userId)
        } catch (e: Exception) {
            throw Exception("Something went wrong while logout everywhere: ${e.localizedMessage}", e)
        }
    }

    suspend fun refresh(sessionId: UUID, userId: UUID): Auth {
        try {
            val user = userRepository.findById(userId)

            val (accessToken, refreshToken)  = tokenService.generateTokens(sessionId, user.id, user.email, user.role)

            sessionRepository.updateByIdAndUserId(sessionId, userId, refreshToken)

            return Auth(
                accessToken = accessToken,
                refreshToken = refreshToken,
                user = user
            )
        } catch (e: Exception) {
            throw Exception("Something went wrong while refresh: ${e.localizedMessage}", e)
        }
    }
}