package org.daazay.presentation.response.booking

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BuildingResponse(
    val id: String,
    val name: String,
    val address: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
)