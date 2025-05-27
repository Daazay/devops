package org.daazay.data.mapper

import org.daazay.data.table.UserTable
import org.daazay.domain.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun InsertStatement<Number>.toUser() = User(
    id = this[UserTable.id].value,
    email = this[UserTable.email],
    username = this[UserTable.username],
    password = this[UserTable.password],
    salt = this[UserTable.salt],
    role = this[UserTable.role],
    updatedAt = this[UserTable.updatedAt],
    createdAt = this[UserTable.createdAt],
)

fun ResultRow.toUser() = User(
    id = this[UserTable.id].value,
    email = this[UserTable.email],
    username = this[UserTable.username],
    password = this[UserTable.password],
    salt = this[UserTable.salt],
    role = this[UserTable.role],
    updatedAt = this[UserTable.updatedAt],
    createdAt = this[UserTable.createdAt],
)