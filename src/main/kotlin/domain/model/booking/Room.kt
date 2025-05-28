package org.daazay.domain.model.building

import kotlinx.datetime.LocalDateTime
import java.util.UUID

enum class RoomStatus {
    FREE, OCCUPIED, UNAVAILABLE
}

data class Room(
    val id: UUID,
    val buildingId: UUID,
    val name: String,
    val capacity: Int,
    val status: RoomStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class RoomCreate(
    val buildingId: UUID,
    val name: String,
    val capacity: Int,
    val status: RoomStatus,
)

data class RoomUpdate(
    val name: String? = null,
    val capacity: Int? = null,
    val status: RoomStatus? = null,
)