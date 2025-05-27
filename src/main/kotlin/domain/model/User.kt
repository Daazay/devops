package org.daazay.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.*

data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val password: String,
    val salt: String,
    val role: UserRole,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class UserCreate(
    val username: String,
    val email: String,
    val password: String,
    val salt: String,
    val role: UserRole = UserRole.USER,
)

data class UserUpdate(
    val username: String? = null,
    val password: String? = null,
    val salt: String? = null,
    val role: UserRole? = null,
)
