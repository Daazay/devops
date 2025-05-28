package org.daazay.data.mapper.booking

import org.daazay.data.table.booking.BuildingTable
import org.daazay.domain.model.building.Building
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun InsertStatement<Number>.toBuilding() = Building(
    id = this[BuildingTable.id].value,
    name = this[BuildingTable.name],
    address = this[BuildingTable.address],
    createdAt = this[BuildingTable.createdAt],
    updatedAt = this[BuildingTable.updatedAt],
)

fun ResultRow.toBuilding() = Building(
    id = this[BuildingTable.id].value,
    name = this[BuildingTable.name],
    address = this[BuildingTable.address],
    createdAt = this[BuildingTable.createdAt],
    updatedAt = this[BuildingTable.updatedAt],
)