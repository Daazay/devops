package org.daazay.domain.repository.auth

import org.daazay.domain.model.auth.Token
import java.util.UUID

interface SessionRepository {
    suspend fun add(id: UUID, userId: UUID, token: String): Token

    suspend fun updateByIdAndUserId(id: UUID, userId: UUID, token: String)

    suspend fun findById(id: UUID): Token
    suspend fun findByUserId(userId: UUID): List<Token>
    suspend fun getAll(): List<Token>

    suspend fun deleteByIdAndUserId(id: UUID, userId: UUID)
    suspend fun deleteByUserId(userId: UUID): Int
    suspend fun deleteAll(): Int
}