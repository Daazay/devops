package org.daazay.data.mapper

import org.daazay.data.table.SessionTable
import org.daazay.domain.model.Token
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun InsertStatement<Number>.toToken() = Token(
    id = this[SessionTable.id],
    userId = this[SessionTable.userId].value,
    token = this[SessionTable.token],
    updatedAt = this[SessionTable.updatedAt],
    createdAt = this[SessionTable.createdAt],
)

fun ResultRow.toToken() = Token(
    id = this[SessionTable.id],
    userId = this[SessionTable.userId].value,
    token = this[SessionTable.token],
    updatedAt = this[SessionTable.updatedAt],
    createdAt = this[SessionTable.createdAt],
)