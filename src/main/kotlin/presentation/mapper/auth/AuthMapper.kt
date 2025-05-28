package org.daazay.presentation.mapper.auth

import org.daazay.domain.model.auth.Auth
import org.daazay.presentation.response.auth.AuthResponse

fun Auth.toAuthResponse() = AuthResponse(
    user = user.toUserResponse(),
    accessToken = accessToken,
    refreshToken = refreshToken,
)