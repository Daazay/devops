package org.daazay.presentation.request

import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateRequest(
    val username: String? = null,
    val password: String? = null,
    val email: String? = null,
    val role: String? = null,
)