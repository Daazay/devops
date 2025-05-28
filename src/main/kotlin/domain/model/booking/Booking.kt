package org.daazay.domain.model.building

import kotlinx.datetime.LocalDateTime
import java.util.*

enum class BookingStatus {
    CONFIRMED, PENDING, CANCELLED
}

data class Booking(
    val id: UUID,
    val userId: UUID,
    val roomId: UUID,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val status: BookingStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class BookingCreate(
    val userId: UUID,
    val roomId: UUID,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val status: BookingStatus,
)

data class BookingUpdate(
    val roomId: UUID? = null,
    val startTime: LocalDateTime? = null,
    val endTime: LocalDateTime? = null,
    val status: BookingStatus? = null,
)