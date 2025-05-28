package org.daazay.presentation.request.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoomCreateRequest(
    @SerialName("building_id")
    val buildingId: String,
    val name: String,
    val capacity: Int,
    val status: String? = null,
)

@Serializable
data class RoomUpdateRequest(
    val name: String? = null,
    val capacity: Int? = null,
    val status: String? = null,
)