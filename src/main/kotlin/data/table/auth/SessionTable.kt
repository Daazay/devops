package org.daazay.data.table.auth

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object SessionTable : Table("sessions") {
    val id = uuid("id")
    val userId = reference("user_id", UserTable.id, ReferenceOption.CASCADE)
    val token = varchar("token", 512)
    val updatedAt = datetime("updated_at")
    val createdAt = datetime("created_at")

    init {
        PrimaryKey(id, userId)
    }
}