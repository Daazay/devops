package org.daazay.presentation.mapper

import org.daazay.domain.model.Auth
import org.daazay.presentation.response.AuthResponse

fun Auth.toAuthResponse() = AuthResponse(
    user = user.toUserResponse(),
    accessToken = accessToken,
    refreshToken = refreshToken,
)