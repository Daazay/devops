package org.daazay.presentation.mapper.booking

import org.daazay.domain.model.building.Room
import org.daazay.presentation.response.booking.RoomResponse

fun Room.toRoomResponse() = RoomResponse(
    id = id.toString(),
    buildingId = buildingId.toString(),
    name = name,
    status = status.name,
    capacity = capacity,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)