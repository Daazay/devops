package org.daazay.presentation.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class AuthResponse(
    val user: UserResponse,
    @SerialName("access_token")
    val accessToken: String,
    @Transient
    val refreshToken: String = "",
)