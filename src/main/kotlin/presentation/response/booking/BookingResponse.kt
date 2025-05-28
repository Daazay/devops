package org.daazay.presentation.response.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingResponse(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("room_id")
    val roomId: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)