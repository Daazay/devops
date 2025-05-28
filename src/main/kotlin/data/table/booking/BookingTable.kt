package org.daazay.data.table.booking

import org.daazay.data.table.auth.UserTable
import org.daazay.domain.model.building.BookingStatus
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object BookingTable : UUIDTable("bookings") {
    val userId = reference("user_id", UserTable.id, onDelete = ReferenceOption.CASCADE)
    val roomId = reference("room_id", RoomTable.id, onDelete = ReferenceOption.CASCADE)
    val startTime = datetime("start_time")
    val endTime = datetime("end_time")
    val status = enumeration("status", BookingStatus::class)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}