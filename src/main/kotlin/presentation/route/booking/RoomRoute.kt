package org.daazay.presentation.route.booking

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.daazay.presentation.controller.booking.RoomController
import org.daazay.presentation.request.booking.RoomCreateRequest
import org.daazay.presentation.request.booking.RoomUpdateRequest
import org.daazay.utils.Result
import org.koin.java.KoinJavaComponent.getKoin
import java.util.*

fun Route.configureRoomRoute(
    controller: RoomController = getKoin().get<RoomController>(),
) {
    route("/rooms") {
        authenticate("user") {
            post("/") {
                try {
                    val request = call.receiveNullable<RoomCreateRequest>()
                        ?: return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid request"))
                    when (val result = controller.create(request)) {
                        is Result.Error -> {
                            return@post call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                        }
                        is Result.Success -> {
                            return@post call.respond(HttpStatusCode.OK, result.data!!)
                        }
                    }
                } catch (e: Exception) {
                    return@post call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
                }
            }
        }

        authenticate("user") {
            put("/{id}") {
                try {
                    val id = call.parameters["id"]?.let { id -> UUID.fromString(id) }
                        ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid id"))
                    val request = call.receiveNullable<RoomUpdateRequest>()
                        ?: return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid request"))
                    when (val result = controller.updateById(id, request)) {
                        is Result.Error -> {
                            return@put call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                        }
                        is Result.Success -> {
                            return@put call.respond(HttpStatusCode.OK)
                        }
                    }
                } catch (e: Exception) {
                    return@put call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
                }
            }
        }

        get("/{id}") {
            try {
                val id = call.parameters["id"]?.let { id -> UUID.fromString(id) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid id"))
                when(val result = controller.findById(id)) {
                    is Result.Error -> {
                        return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                    }
                    is Result.Success -> {
                        return@get call.respond(HttpStatusCode.OK, result.data!!)
                    }
                }
            } catch (e: Exception) {
                return@get call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
            }
        }

        get("/building/{buildingId}") {
            try {
                val id = call.parameters["buildingId"]?.let { id -> UUID.fromString(id) }
                    ?: return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid id"))
                when(val result = controller.getByBuildingId(id)) {
                    is Result.Error -> {
                        return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                    }
                    is Result.Success -> {
                        return@get call.respond(HttpStatusCode.OK, result.data!!)
                    }
                }
            } catch (e: Exception) {
                return@get call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
            }
        }

        get("/all") {
            try {
                when(val result = controller.getAll()) {
                    is Result.Error -> {
                        return@get call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                    }
                    is Result.Success -> {
                        return@get call.respond(HttpStatusCode.OK, result.data!!)
                    }
                }
            } catch (e: Exception) {
                return@get call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
            }
        }

        authenticate("user") {
            delete("/{id}") {
                try {
                    val id = call.parameters["id"]?.let { id -> UUID.fromString(id) }
                        ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid id"))
                    when (val result = controller.deleteById(id)) {
                        is Result.Error -> {
                            return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                        }
                        is Result.Success -> {
                            return@delete call.respond(HttpStatusCode.OK)
                        }
                    }
                } catch (e: Exception) {
                    return@delete call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
                }
            }
        }

        authenticate("user") {
            delete("/building/{buildingId}") {
                try {
                    val id = call.parameters["buildingId"]?.let { id -> UUID.fromString(id) }
                        ?: return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid id"))
                    when (val result = controller.deleteByBuildingId(id)) {
                        is Result.Error -> {
                            return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                        }
                        is Result.Success -> {
                            return@delete call.respond(HttpStatusCode.OK, result.data!!)
                        }
                    }
                } catch (e: Exception) {
                    return@delete call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
                }
            }
        }

        authenticate("user") {
            delete("/all") {
                try {
                    when (val result = controller.deleteAll()) {
                        is Result.Error -> {
                            return@delete call.respond(HttpStatusCode.BadRequest, mapOf("error" to result.message))
                        }
                        is Result.Success -> {
                            return@delete call.respond(HttpStatusCode.OK)
                        }
                    }
                } catch (e: Exception) {
                    return@delete call.respond(HttpStatusCode.InternalServerError, mapOf("error" to e.toString()))
                }
            }
        }
    }
}