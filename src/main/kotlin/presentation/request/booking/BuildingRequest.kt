package org.daazay.presentation.request.booking

import kotlinx.serialization.Serializable

@Serializable
data class BuildingCreateRequest(
    val name: String,
    val address: String,
)

@Serializable
data class BuildingUpdateRequest(
    val name: String? = null,
    val address: String? = null,
)