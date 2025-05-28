package org.daazay.domain.repository.building

import org.daazay.domain.model.building.Building
import org.daazay.domain.model.building.BuildingCreate
import org.daazay.domain.model.building.BuildingUpdate
import java.util.*

interface BuildingRepository {
    suspend fun add(building: BuildingCreate): Building

    suspend fun updateById(id: UUID, building: BuildingUpdate)

    suspend fun findById(id: UUID): Building
    suspend fun getAll(): List<Building>

    suspend fun deleteById(id: UUID)
    suspend fun deleteAll(): Int
}