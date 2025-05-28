package org.daazay.presentation.request.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingCreateRequest(
    @SerialName("user_id")
    val userId: String,
    @SerialName("room_id")
    val roomId: String,
    @SerialName("start_time")
    val startTime: String,
    @SerialName("end_time")
    val endTime: String,
)

@Serializable
data class BookingUpdateRequest(
    @SerialName("room_id")
    val roomId: String? = null,
    @SerialName("start_time")
    val startTime: String? = null,
    @SerialName("end_time")
    val endTime: String? = null,
    val status: String? = null,
)