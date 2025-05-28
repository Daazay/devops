package org.daazay.domain.model.auth

data class Auth(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
)