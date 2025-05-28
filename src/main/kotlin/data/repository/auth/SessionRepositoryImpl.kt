package org.daazay.data.repository.auth

import org.daazay.data.mapper.auth.toToken
import org.daazay.data.table.auth.SessionTable
import org.daazay.data.utils.tranzaction
import org.daazay.domain.model.auth.Token
import org.daazay.domain.repository.auth.SessionRepository
import java.util.*
import kotlinx.datetime.Clock.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlin.NoSuchElementException

class SessionRepositoryImpl : SessionRepository {
    init {
        tranzaction { SchemaUtils.create(SessionTable) }
    }

    override suspend fun add(id: UUID, userId: UUID, token: String): Token = tranzaction(
        onUniqueConstraintViolation = {
            throw IllegalArgumentException("Session token with same userId and session code already exists")
        }
    ) {
        SessionTable.insert {
            it[SessionTable.id] = id
            it[SessionTable.userId] = userId
            it[SessionTable.token] = token
            it[SessionTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
            it[SessionTable.createdAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }.toToken()
    }

    override suspend fun updateByIdAndUserId(id: UUID, userId: UUID, token: String): Unit = tranzaction {
        if (SessionTable.selectAll().where { (SessionTable.id eq id) and (SessionTable.userId eq userId) }.empty()) {
            throw NoSuchElementException("Session token with id $id and userId $userId does no exist")
        }

        SessionTable.update({ (SessionTable.id eq id) and (SessionTable.userId eq userId) }) {
            it[SessionTable.token] = token
            it[SessionTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    override suspend fun findById(id: UUID): Token = tranzaction {
        SessionTable.selectAll()
            .where { SessionTable.id eq id }
            .singleOrNull()
            ?.toToken()
            ?: throw NoSuchElementException("Session token with id $id does not exist")
    }

    override suspend fun findByUserId(userId: UUID): List<Token> = tranzaction {
        SessionTable.selectAll()
            .where { SessionTable.userId eq userId }
            .map { it.toToken() }
    }

    override suspend fun getAll(): List<Token> = tranzaction {
        SessionTable.selectAll().map { it.toToken() }
    }

    override suspend fun deleteByIdAndUserId(id: UUID, userId: UUID) = tranzaction {
        if (SessionTable.deleteWhere { (SessionTable.id eq id) and (SessionTable.userId eq userId) } == 0) {
            throw NoSuchElementException("Session token with id $id and userId $userId does not exist")
        }
    }

    override suspend fun deleteByUserId(userId: UUID): Int = tranzaction {
        SessionTable.deleteWhere { SessionTable.userId eq userId }
    }

    override suspend fun deleteAll(): Int = tranzaction {
        SessionTable.deleteAll()
    }
}