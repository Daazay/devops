package org.daazay.data.table.auth

import org.daazay.domain.model.auth.UserRole
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object UserTable : UUIDTable("users") {
    val username = varchar("username", 128)
    val email = varchar("email", 256).uniqueIndex()
    val password = varchar("password", 64)
    val role = enumeration("role", UserRole::class)
    val salt = varchar("salt", 64)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}