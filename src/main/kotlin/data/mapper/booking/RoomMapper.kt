package org.daazay.data.mapper.booking

import org.daazay.data.table.booking.RoomTable
import org.daazay.domain.model.building.Room
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun InsertStatement<Number>.toRoom() = Room(
    id = this[RoomTable.id].value,
    buildingId = this[RoomTable.buildingId].value,
    name = this[RoomTable.name],
    capacity = this[RoomTable.capacity],
    status = this[RoomTable.status],
    createdAt = this[RoomTable.createdAt],
    updatedAt = this[RoomTable.updatedAt],
)

fun ResultRow.toRoom() = Room(
    id = this[RoomTable.id].value,
    buildingId = this[RoomTable.buildingId].value,
    name = this[RoomTable.name],
    capacity = this[RoomTable.capacity],
    status = this[RoomTable.status],
    createdAt = this[RoomTable.createdAt],
    updatedAt = this[RoomTable.updatedAt],
)