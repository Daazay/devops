package org.daazay.presentation.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthSignupRequest(
    val username: String,
    val email: String,
    val password: String,
)

@Serializable
data class AuthLoginRequest(
    val email: String,
    val password: String,
)