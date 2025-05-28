package org.daazay.data.table.booking

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object BuildingTable : UUIDTable("buildings") {
    val name = varchar("name", 128)
    val address = varchar("address", 512)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("update_at").defaultExpression(CurrentDateTime)
}