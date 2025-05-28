package org.daazay.domain.model.auth

import kotlinx.datetime.LocalDateTime
import java.util.*

data class Token(
    val id: UUID,
    val userId: UUID,
    val token: String,
    val updatedAt: LocalDateTime,
    val createdAt: LocalDateTime,
)