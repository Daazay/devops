package org.daazay.domain.repository.building

import org.daazay.domain.model.building.Booking
import org.daazay.domain.model.building.BookingCreate
import org.daazay.domain.model.building.BookingUpdate
import java.util.*

interface BookingRepository {
    suspend fun add(booking: BookingCreate): Booking

    suspend fun updateById(id: UUID, booking: BookingUpdate)

    suspend fun findById(id: UUID): Booking
    suspend fun findByRoomId(roomId: UUID): List<Booking>
    suspend fun findByUserId(userId: UUID): List<Booking>
    suspend fun getAll(): List<Booking>

    suspend fun deleteById(id: UUID)
    suspend fun deleteByRoomId(roomId: UUID): Int
    suspend fun deleteByUserId(userId: UUID): Int
    suspend fun deleteAll(): Int
}