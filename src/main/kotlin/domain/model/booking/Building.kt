package org.daazay.domain.model.building

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Building(
    val id: UUID,
    val name: String,
    val address: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

data class BuildingCreate(
    val name: String,
    val address: String,
)

data class BuildingUpdate(
    val name: String? = null,
    val address: String? = null,
)