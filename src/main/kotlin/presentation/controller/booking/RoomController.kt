package org.daazay.presentation.controller.booking

import org.daazay.domain.service.building.RoomService
import org.daazay.presentation.mapper.booking.toRoomResponse
import org.daazay.presentation.request.booking.RoomCreateRequest
import org.daazay.presentation.request.booking.RoomUpdateRequest
import org.daazay.presentation.response.booking.RoomResponse
import org.daazay.utils.Result
import org.daazay.utils.withResult
import java.util.*

class RoomController(
    private val service: RoomService
) {
    suspend fun create(request: RoomCreateRequest): Result<RoomResponse> = withResult {
        service.create(request).toRoomResponse()
    }

    suspend fun findById(id: UUID): Result<RoomResponse> = withResult {
        service.findById(id).toRoomResponse()
    }

    suspend fun getByBuildingId(id: UUID): Result<List<RoomResponse>> = withResult {
        service.findByBuildingId(id).map { it.toRoomResponse() }
    }

    suspend fun getAll(): Result<List<RoomResponse>> = withResult {
        service.getAll().map { it.toRoomResponse() }
    }

    suspend fun updateById(id: UUID, request: RoomUpdateRequest): Result<Unit> = withResult {
        service.updateById(id, request)
    }

    suspend fun deleteById(id: UUID): Result<Unit> = withResult {
        service.deleteById(id)
    }

    suspend fun deleteByBuildingId(id: UUID): Result<Unit> = withResult {
        service.deleteByBuildingId(id)
    }

    suspend fun deleteAll(): Result<Int> = withResult {
        service.deleteAll()
    }
}