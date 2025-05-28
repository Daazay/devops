package org.daazay.domain.service.building

import org.daazay.domain.model.building.*
import org.daazay.domain.repository.building.RoomRepository
import org.daazay.presentation.request.booking.RoomCreateRequest
import org.daazay.presentation.request.booking.RoomUpdateRequest
import java.util.*

class RoomService(
    private val repository: RoomRepository
) {
    private fun validateName(name: String) {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(name.length <= 64) { "Name cannot be more than 64 characters long" }
    }
    private fun validateCapacity(capacity: Int) {
        require(capacity in 1..100) {
            "Capacity must be greater than 0 and lesser than 100"
        }
    }

    suspend fun create(room: RoomCreateRequest): Room {
        try {
            validateName(room.name)
            validateCapacity(room.capacity)

            return repository.add(
                RoomCreate(
                    buildingId = UUID.fromString(room.buildingId),
                    name = room.name,
                    capacity = room.capacity,
                    status = RoomStatus.FREE,
                )
            )
        } catch (e: Exception) {
            throw Exception("Something went wrong while adding room: ${e.localizedMessage}", e)
        }
    }

    suspend fun updateById(id: UUID, room: RoomUpdateRequest) {
        try {
            if (room.name != null) validateName(room.name)
            if (room.capacity != null) validateCapacity(room.capacity)

            repository.updateById(id,
                RoomUpdate(
                    name = room.name,
                    capacity = room.capacity,
                    status = room.status?.let { RoomStatus.valueOf(it) },
                )
            )
        } catch (e: Exception) {
            throw Exception("Something went wrong while updating room: ${e.localizedMessage}", e)
        }
    }

    suspend fun findById(id: UUID): Room {
        try {
            return repository.findById(id)
        } catch (e: Exception) {
            throw Exception("Something went wrong while finding room by id: ${e.localizedMessage}", e)
        }
    }

    suspend fun findByBuildingId(buildingId: UUID): List<Room> {
        try {
            return repository.findByBuildingId(buildingId)
        } catch (e: Exception) {
            throw Exception("Something went wrong while finding rooms by buildingId: ${e.localizedMessage}", e)
        }
    }

    suspend fun getAll(): List<Room> {
        try {
            return repository.getAll()
        } catch (e: Exception) {
            throw Exception("Something went wrong while getting all rooms: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteById(id: UUID) {
        try {
            return repository.deleteById(id)
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting room by id: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteByBuildingId(id: UUID): Int {
        try {
            return repository.deleteByBuildingId(id)
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting rooms by buildingId: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteAll(): Int {
        try {
            return repository.deleteAll()
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting all rooms: ${e.localizedMessage}", e)
        }
    }
}