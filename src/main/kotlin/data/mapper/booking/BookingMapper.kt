package org.daazay.data.mapper.booking

import org.daazay.data.table.booking.BookingTable
import org.daazay.domain.model.building.Booking
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.statements.InsertStatement

fun InsertStatement<Number>.toBooking() = Booking(
    id = this[BookingTable.id].value,
    userId = this[BookingTable.userId].value,
    roomId = this[BookingTable.roomId].value,
    startTime = this[BookingTable.startTime],
    endTime = this[BookingTable.endTime],
    status = this[BookingTable.status],
    createdAt = this[BookingTable.createdAt],
    updatedAt = this[BookingTable.updatedAt],
)

fun ResultRow.toBooking() = Booking(
    id = this[BookingTable.id].value,
    userId = this[BookingTable.userId].value,
    roomId = this[BookingTable.roomId].value,
    startTime = this[BookingTable.startTime],
    endTime = this[BookingTable.endTime],
    status = this[BookingTable.status],
    createdAt = this[BookingTable.createdAt],
    updatedAt = this[BookingTable.updatedAt],
)