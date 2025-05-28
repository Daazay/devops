package org.daazay.presentation.controller.booking

import org.daazay.domain.service.building.BuildingService
import org.daazay.presentation.mapper.booking.toBuildingResponse
import org.daazay.presentation.request.booking.BuildingCreateRequest
import org.daazay.presentation.request.booking.BuildingUpdateRequest
import org.daazay.presentation.response.booking.BuildingResponse
import org.daazay.utils.Result
import org.daazay.utils.withResult
import java.util.*

class BuildingController(
    private val service: BuildingService
) {
    suspend fun create(request: BuildingCreateRequest): Result<BuildingResponse> = withResult {
        service.create(request).toBuildingResponse()
    }

    suspend fun findById(id: UUID): Result<BuildingResponse> = withResult {
        service.findById(id).toBuildingResponse()
    }

    suspend fun getAll(): Result<List<BuildingResponse>> = withResult {
        service.getAll().map { it.toBuildingResponse() }
    }

    suspend fun updateById(id: UUID, building: BuildingUpdateRequest): Result<Unit> = withResult {
        service.updateById(id, building)
    }

    suspend fun deleteById(id: UUID): Result<Unit> = withResult {
        service.deleteById(id)
    }

    suspend fun deleteAll(): Result<Int> = withResult {
        service.deleteAll()
    }
}