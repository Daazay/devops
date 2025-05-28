package org.daazay.domain.service.building

import kotlinx.datetime.Clock.System
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.daazay.domain.model.building.*
import org.daazay.domain.repository.building.BookingRepository
import org.daazay.presentation.request.booking.BookingCreateRequest
import org.daazay.presentation.request.booking.BookingUpdateRequest
import java.util.*

class BookingService(
    private val repository: BookingRepository
) {
    private fun validateDates(start: LocalDateTime, end: LocalDateTime) {
        require(start < end) { "start must be greater than end" }
        require(start > System.now().toLocalDateTime(TimeZone.UTC)) {
            "startTime must be in the future"
        }
    }

    suspend fun create(booking: BookingCreateRequest): Booking {
        try {
            val startDate = LocalDateTime.parse(booking.startTime)
            val endDate = LocalDateTime.parse(booking.endTime)
            validateDates(startDate, endDate)

            return repository.add(
                BookingCreate(
                    userId = UUID.fromString(booking.userId),
                    roomId = UUID.fromString(booking.roomId),
                    startTime = startDate,
                    endTime = endDate,
                    status = BookingStatus.CONFIRMED,
                )
            )
        } catch (e: Exception) {
            throw Exception("Something went wrong while adding booking: ${e.localizedMessage}", e)
        }
    }

    suspend fun updateById(id: UUID, booking: BookingUpdateRequest) {
        try {
            val startDate = booking.startTime?.let { LocalDateTime.parse(it) }
            val endDate = booking.endTime?.let { LocalDateTime.parse(it) }

            require(booking.status == null || (startDate == null && endDate == null)) {
                "Modifications must be made to either the booking state or the time parameters, not both simultaneously"
            }

            if (startDate != null && endDate != null) {
                validateDates(startDate, endDate)
            }

            repository.updateById(id,
                BookingUpdate(
                    roomId = booking.roomId?.let { UUID.fromString(it) },
                    startTime = startDate,
                    endTime = endDate,
                )
            )
        } catch (e: Exception) {
            throw Exception("Something went wrong while updating booking: ${e.localizedMessage}", e)
        }
    }

    suspend fun findById(id: UUID): Booking {
        try {
            return repository.findById(id)
        } catch (e: Exception) {
            throw Exception("Something went wrong while finding booking by id: ${e.localizedMessage}", e)
        }
    }

    suspend fun findByRoomId(roomId: UUID): List<Booking> {
        try {
            return repository.findByRoomId(roomId)
        } catch (e: Exception) {
            throw Exception("Something went wrong while finding bookings by roomId: ${e.localizedMessage}", e)
        }
    }

    suspend fun findByUserId(userId: UUID): List<Booking> {
        try {
            return repository.findByUserId(userId)
        } catch (e: Exception) {
            throw Exception("Something went wrong while finding bookings by userId: ${e.localizedMessage}", e)
        }
    }

    suspend fun getAll(): List<Booking> {
        try {
            return repository.getAll()
        } catch (e: Exception) {
            throw Exception("Something went wrong while getting all bookings: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteById(id: UUID) {
        try {
            return repository.deleteById(id)
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting booking by id: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteByUserId(userId: UUID): Int {
        try {
            return repository.deleteByUserId(userId)
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting bookings by userId: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteByRoomId(roomId: UUID): Int {
        try {
            return repository.deleteByRoomId(roomId)
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting bookings by roomId: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteAll(): Int {
        try {
            return repository.deleteAll()
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting all bookings: ${e.localizedMessage}", e)
        }
    }
}