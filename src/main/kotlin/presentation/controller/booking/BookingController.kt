package org.daazay.presentation.controller.booking

import org.daazay.domain.service.building.BookingService
import org.daazay.presentation.mapper.booking.toBookingResponse
import org.daazay.presentation.request.booking.BookingCreateRequest
import org.daazay.presentation.request.booking.BookingUpdateRequest
import org.daazay.presentation.response.booking.BookingResponse
import org.daazay.utils.Result
import org.daazay.utils.withResult
import org.koin.java.KoinJavaComponent.inject
import java.util.*

class BookingController(
    private val service: BookingService
) {
    suspend fun create(request: BookingCreateRequest): Result<BookingResponse> = withResult {
        service.create(request).toBookingResponse()
    }

    suspend fun findById(id: UUID): Result<BookingResponse> = withResult {
        service.findById(id).toBookingResponse()
    }

    suspend fun getAll(): Result<List<BookingResponse>> = withResult {
        service.getAll().map { it.toBookingResponse() }
    }

    suspend fun updateById(id: UUID, request: BookingUpdateRequest): Result<Unit> = withResult {
        service.updateById(id, request)
    }

    suspend fun deleteById(id: UUID): Result<Unit> = withResult {
        service.deleteById(id)
    }

    suspend fun deleteAll(): Result<Int> = withResult {
        service.deleteAll()
    }
}