package org.daazay.data.repository.booking

import kotlinx.datetime.Clock.System
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.daazay.data.mapper.booking.toBooking
import org.daazay.data.table.booking.BookingTable
import org.daazay.data.utils.tranzaction
import org.daazay.domain.model.building.Booking
import org.daazay.domain.model.building.BookingCreate
import org.daazay.domain.model.building.BookingStatus
import org.daazay.domain.model.building.BookingUpdate
import org.daazay.domain.repository.building.BookingRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class BookingRepositoryImpl : BookingRepository {
    init {
        transaction { SchemaUtils.create(BookingTable) }
    }

    override suspend fun add(booking: BookingCreate): Booking = tranzaction(
        onUniqueConstraintViolation = {
            throw IllegalArgumentException("Booking with same email already exists")
        }
    ) {
        if (!BookingTable.selectAll().where {
            (BookingTable.roomId eq booking.roomId) and
            (BookingTable.startTime lessEq booking.endTime) and
            (BookingTable.endTime greaterEq booking.startTime) and
            (BookingTable.status neq BookingStatus.CANCELLED)
        }.empty()) {
            throw IllegalArgumentException("Room is already booked for the selected time period")
        }

        BookingTable.insert {
            it[BookingTable.userId] = booking.userId
            it[BookingTable.roomId] = booking.roomId
            it[BookingTable.status] = booking.status
            it[BookingTable.startTime] = booking.startTime
            it[BookingTable.endTime] = booking.endTime
            it[BookingTable.createdAt] = System.now().toLocalDateTime(TimeZone.UTC)
            it[BookingTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }.toBooking()
    }

    override suspend fun updateById(id: UUID, booking: BookingUpdate): Unit = tranzaction {
        val existing = BookingTable.selectAll().where { BookingTable.id eq id }
            .singleOrNull()
            ?: throw NoSuchElementException("Booking with id $id does not exist")

        val existingStartTime = existing[BookingTable.startTime]
        val existingEndTime = existing[BookingTable.startTime]

        val startTime = booking.startTime ?: existingStartTime
        val endTime = booking.endTime ?: existingEndTime

        if (!BookingTable.selectAll().where {
            (BookingTable.id eq id) and
            (BookingTable.roomId eq booking.roomId) and
            (BookingTable.startTime lessEq endTime) and
            (BookingTable.endTime greaterEq startTime) and
            (BookingTable.status neq BookingStatus.CANCELLED)
        }.empty()) {
            throw IllegalArgumentException("Room is already booked for the selected time period")
        }

        BookingTable.update({ BookingTable.id eq id }) {
            booking.roomId?.let { v -> it[BookingTable.roomId] = v }
            it[BookingTable.startTime] = startTime
            it[BookingTable.endTime] = endTime
            booking.status?.let { v -> it[BookingTable.status] = v }
            it[BookingTable.updatedAt] = System.now().toLocalDateTime(TimeZone.UTC)
        }
    }

    override suspend fun findById(id: UUID): Booking = tranzaction {
        BookingTable.selectAll()
            .where { BookingTable.id eq id }
            .singleOrNull()
            ?.toBooking()
            ?: throw NoSuchElementException("Booking with id $id does not exist")
    }

    override suspend fun findByRoomId(roomId: UUID): List<Booking> = tranzaction {
        BookingTable.selectAll()
            .where { BookingTable.roomId eq roomId }
            .map { it.toBooking() }
    }

    override suspend fun findByUserId(userId: UUID): List<Booking> = tranzaction {
        BookingTable.selectAll()
            .where { BookingTable.userId eq userId }
            .map { it.toBooking() }
    }

    override suspend fun getAll(): List<Booking> = tranzaction {
        BookingTable.selectAll().map { it.toBooking() }
    }

    override suspend fun deleteById(id: UUID): Unit = tranzaction {
        BookingTable.deleteWhere { BookingTable.id eq id }
    }

    override suspend fun deleteByRoomId(roomId: UUID): Int = tranzaction {
        BookingTable.deleteWhere { BookingTable.roomId eq roomId }
    }

    override suspend fun deleteByUserId(userId: UUID): Int = tranzaction {
        BookingTable.deleteWhere { BookingTable.userId eq userId }
    }

    override suspend fun deleteAll(): Int = tranzaction {
        BookingTable.deleteAll()
    }
}