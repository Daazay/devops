package org.daazay.domain.repository

import org.daazay.domain.model.User
import org.daazay.domain.model.UserCreate
import org.daazay.domain.model.UserUpdate
import java.util.UUID

interface UserRepository {
    suspend fun add(user: UserCreate): User

    suspend fun updateById(userId: UUID, user: UserUpdate)
    suspend fun updateByEmail(email: String, user: UserUpdate)

    suspend fun findById(userId: UUID): User
    suspend fun findByEmail(email: String): User
    suspend fun getAll(): List<User>

    suspend fun deleteById(userId: UUID)
    suspend fun deleteAll(): Int
}