package org.daazay.presentation.mapper.booking

import org.daazay.domain.model.building.Booking
import org.daazay.presentation.response.booking.BookingResponse

fun Booking.toBookingResponse() = BookingResponse(
    id = id.toString(),
    userId = userId.toString(),
    roomId = roomId.toString(),
    startTime = startTime.toString(),
    endTime = endTime.toString(),
    status = status.name,
    createdAt = createdAt.toString(),
    updatedAt = updatedAt.toString(),
)