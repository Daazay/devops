package org.daazay.presentation.response.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomResponse(
    val id: String,
    @SerialName("building_id")
    val buildingId: String,
    val name: String,
    val capacity: Int,
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)