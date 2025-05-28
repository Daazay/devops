package org.daazay.domain.service.building

import org.daazay.domain.model.building.Building
import org.daazay.domain.model.building.BuildingCreate
import org.daazay.domain.model.building.BuildingUpdate
import org.daazay.domain.repository.building.BuildingRepository
import org.daazay.presentation.request.booking.BuildingCreateRequest
import org.daazay.presentation.request.booking.BuildingUpdateRequest
import java.util.*

class BuildingService(
    private val repository: BuildingRepository
) {
    private fun validateName(name: String) {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(name.length <= 128) { "Name cannot be more than 128 characters long" }
    }
    private fun validateAddress(address: String) {
        require(address.isNotBlank()) { "Address cannot be blank" }
        require(address.length <= 512) { "Address cannot be more than 512 characters long" }
    }

    suspend fun create(building: BuildingCreateRequest): Building {
        try {
            validateName(building.name)
            validateName(building.address)

            return repository.add(BuildingCreate(
                name = building.name,
                address = building.address,
            ))
        } catch (e: Exception) {
            throw Exception("Something went wrong while adding building: ${e.localizedMessage}", e)
        }
    }

    suspend fun updateById(id: UUID, building: BuildingUpdateRequest) {
        try {
            if (building.name != null) validateName(building.name)
            if (building.address != null) validateName(building.address)

            return repository.updateById(id, BuildingUpdate(
                name = building.name,
                address = building.address,
            ))
        } catch (e: Exception) {
            throw Exception("Something went wrong while updating building: ${e.localizedMessage}", e)
        }
    }

    suspend fun findById(id: UUID): Building {
        try {
            return repository.findById(id)
        } catch (e: Exception) {
            throw Exception("Something went wrong while finding building by id: ${e.localizedMessage}", e)
        }
    }

    suspend fun getAll(): List<Building> {
        try {
            return repository.getAll()
        } catch (e: Exception) {
            throw Exception("Something went wrong while getting all building: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteById(id: UUID) {
        try {
            return repository.deleteById(id)
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting building by id: ${e.localizedMessage}", e)
        }
    }

    suspend fun deleteAll(): Int {
        try {
            return repository.deleteAll()
        } catch (e: Exception) {
            throw Exception("Something went wrong while deleting all building: ${e.localizedMessage}", e)
        }
    }
}