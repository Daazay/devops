package org.daazay.domain.repository.building

import org.daazay.domain.model.building.Room
import org.daazay.domain.model.building.RoomCreate
import org.daazay.domain.model.building.RoomUpdate
import java.util.*

interface RoomRepository {
    suspend fun add(room: RoomCreate): Room

    suspend fun updateById(id: UUID, room: RoomUpdate)

    suspend fun findById(id: UUID): Room
    suspend fun findByBuildingId(buildingId: UUID): List<Room>
    suspend fun getAll(): List<Room>

    suspend fun deleteById(id: UUID)
    suspend fun deleteByBuildingId(buildingId: UUID): Int
    suspend fun deleteAll(): Int
}