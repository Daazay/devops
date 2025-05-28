package org.daazay.data.table.booking

import org.daazay.domain.model.building.RoomStatus
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object RoomTable : UUIDTable("rooms") {
    val buildingId = reference("building_id", BuildingTable.id, onDelete = ReferenceOption.CASCADE)
    val name = varchar("name", 64)
    val capacity = integer("capacity")
    val status = enumeration("type", RoomStatus::class)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

    init {
        index(true, buildingId, name)
    }
}