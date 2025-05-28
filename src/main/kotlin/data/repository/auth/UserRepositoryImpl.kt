package org.daazay.data.repository.auth

import org.daazay.data.table.auth.UserTable
import org.daazay.data.utils.tranzaction
import org.daazay.domain.model.auth.User
import org.daazay.domain.model.auth.UserCreate
import org.daazay.domain.model.auth.UserUpdate
import org.daazay.domain.repository.auth.UserRepository
import kotlinx.datetime.Clock.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.daazay.data.mapper.auth.toUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*
import kotlin.NoSuchElementException

class UserRepositoryImpl: UserRepository {
    init {
        tranzaction { SchemaUtils.create(UserTable) }
    }

    override suspend fun add(user: UserCreate): User = tranzaction(
        onUniqueConstraintViolation = {
            throw IllegalArgumentException("User with same email already exists")
        }
    ) {
        UserTable.insert {
            it[UserTable.email] = user.email
            it[UserTable.username] = user.username
            it[UserTable.password] = user.password
            it[UserTable.salt] = user.salt
            it[UserTable.role] = user.role
            it[UserTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
            it[UserTable.createdAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }.toUser()
    }

    override suspend fun updateById(userId: UUID, user: UserUpdate): Unit = tranzaction {
        if (UserTable.selectAll().where { UserTable.id eq userId }.empty()) {
            throw NoSuchElementException("User with id $userId does not exist")
        }

        UserTable.update({ UserTable.id eq userId }) {
            user.username?.let { v -> it[UserTable.username] = v }
            user.password?.let { v -> it[UserTable.password] = v }
            user.salt?.let { v -> it[UserTable.salt] = v }
            user.role?.let { v -> it[UserTable.role] = v }
            it[UserTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    override suspend fun updateByEmail(email: String, user: UserUpdate): Unit = tranzaction {
        if (UserTable.selectAll().where { UserTable.email eq email }.empty()) {
            throw NoSuchElementException("User with email $email does not exist")
        }

        UserTable.update({ UserTable.email eq email }) {
            user.username?.let { v -> it[UserTable.username] = v }
            user.password?.let { v -> it[UserTable.password] = v }
            user.salt?.let { v -> it[UserTable.salt] = v }
            user.role?.let { v -> it[UserTable.role] = v }
            it[UserTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    override suspend fun findById(userId: UUID): User = tranzaction {
        UserTable.selectAll()
            .where { UserTable.id eq userId }
            .singleOrNull()
            ?.toUser()
                ?: throw NoSuchElementException("User with id $userId does not exist")
    }

    override suspend fun findByEmail(email: String): User = tranzaction {
        UserTable.selectAll()
            .where { UserTable.email eq email }
            .singleOrNull()
            ?.toUser()
            ?: throw NoSuchElementException("User with email $email does not exist")
    }

    override suspend fun getAll(): List<User> = tranzaction {
        UserTable.selectAll().map { it.toUser() }
    }

    override suspend fun deleteById(userId: UUID): Unit = tranzaction {
        if (UserTable.deleteWhere { UserTable.id eq userId } == 0) {
            throw NoSuchElementException("User with id $userId does not exist")
        }
    }

    override suspend fun deleteAll(): Int = tranzaction {
        UserTable.deleteAll()
    }
}