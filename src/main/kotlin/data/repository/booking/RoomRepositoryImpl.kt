package org.daazay.data.repository.booking

import kotlinx.datetime.Clock.System
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.daazay.data.mapper.booking.toRoom
import org.daazay.data.table.auth.UserTable
import org.daazay.data.table.booking.RoomTable
import org.daazay.data.utils.tranzaction
import org.daazay.domain.model.building.Room
import org.daazay.domain.model.building.RoomCreate
import org.daazay.domain.model.building.RoomUpdate
import org.daazay.domain.repository.building.RoomRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class RoomRepositoryImpl : RoomRepository {
    init {
        transaction { SchemaUtils.create(RoomTable) }
    }

    override suspend fun add(room: RoomCreate): Room = tranzaction(
        onUniqueConstraintViolation = {
            throw IllegalArgumentException("Room with same buildingId and name already exists")
        }
    ) {
        RoomTable.insert {
            it[RoomTable.buildingId] = room.buildingId
            it[RoomTable.name] = room.name
            it[RoomTable.capacity] = room.capacity
            it[RoomTable.status] = room.status
            it[RoomTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
            it[RoomTable.createdAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }.toRoom()
    }

    override suspend fun updateById(id: UUID, room: RoomUpdate): Unit = tranzaction {
        if (RoomTable.selectAll().where { RoomTable.id eq id }.empty()) {
            throw NoSuchElementException("Room with id $id does not exist")
        }

        RoomTable.update({ RoomTable.id eq id }) {
            room.name?.let { v -> it[RoomTable.name] = v }
            room.capacity?.let { v -> it[RoomTable.capacity] = v }
            room.status?.let { v -> it[RoomTable.status] = v }
            it[UserTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    override suspend fun findById(id: UUID): Room = tranzaction {
        RoomTable.selectAll()
            .where { RoomTable.id eq id }
            .singleOrNull()
            ?.toRoom()
            ?: throw NoSuchElementException("Room with id $id does not exist")
    }

    override suspend fun findByBuildingId(buildingId: UUID): List<Room> = tranzaction {
        RoomTable.selectAll()
            .where { RoomTable.buildingId eq buildingId }
            .map { it.toRoom() }
    }

    override suspend fun getAll(): List<Room> = tranzaction {
        RoomTable.selectAll().map { it.toRoom() }
    }

    override suspend fun deleteById(id: UUID): Unit = tranzaction {
        RoomTable.deleteWhere { RoomTable.id eq id }
    }

    override suspend fun deleteByBuildingId(buildingId: UUID): Int = tranzaction {
        RoomTable.deleteWhere { RoomTable.buildingId eq buildingId }
    }

    override suspend fun deleteAll(): Int = tranzaction {
        RoomTable.deleteAll()
    }
}