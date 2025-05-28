package org.daazay.presentation.mapper.booking

import org.daazay.domain.model.building.Building
import org.daazay.presentation.response.booking.BuildingResponse

fun Building.toBuildingResponse() = BuildingResponse(
    id = id.toString(),
    name = name,
    address = address,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString()
)