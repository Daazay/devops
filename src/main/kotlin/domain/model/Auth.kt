package org.daazay.domain.model

data class Auth(
    val user: User,
    val accessToken: String,
    val refreshToken: String,
)