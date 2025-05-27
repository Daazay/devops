package org.daazay.presentation.controller

import org.daazay.domain.service.AuthService
import org.daazay.presentation.mapper.toAuthResponse
import org.daazay.presentation.request.AuthLoginRequest
import org.daazay.presentation.request.AuthSignupRequest
import org.daazay.presentation.response.AuthResponse
import org.daazay.utils.Result
import org.daazay.utils.withResult
import java.util.*

class AuthController(
    private val service: AuthService,
) {
    suspend fun signup(request: AuthSignupRequest): Result<AuthResponse> = withResult {
        service.signup(request).toAuthResponse()
    }

    suspend fun login(request: AuthLoginRequest): Result<AuthResponse> = withResult {
        service.login(request).toAuthResponse()
    }

    suspend fun logoutEverywhere(userId: UUID): Result<Int> = withResult {
        service.logoutEverywhere(userId)
    }

    suspend fun logout(sessionId: UUID, userId: UUID): Result<Unit>  = withResult {
        service.logout(sessionId, userId)
    }

    suspend fun refreshToken(sessionId: UUID, userId: UUID): Result<AuthResponse>  = withResult {
        service.refresh(sessionId, userId).toAuthResponse()
    }
}