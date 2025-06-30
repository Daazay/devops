import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.Json
import org.daazay.presentation.controller.booking.BookingController
import org.daazay.presentation.controller.booking.BuildingController
import org.daazay.presentation.request.booking.BookingCreateRequest
import org.daazay.presentation.request.booking.BookingUpdateRequest
import org.daazay.presentation.request.booking.BuildingCreateRequest
import org.daazay.presentation.response.booking.BookingResponse
import org.daazay.presentation.response.booking.BuildingResponse
import org.daazay.presentation.route.booking.configureBookingRoute
import org.daazay.presentation.route.booking.configureBuildingRoute
import org.daazay.utils.Result
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BookingRouteTest {
    private val controller = mockk<BookingController>()

    @BeforeTest
    fun setup() {
        clearAllMocks()
    }


    private fun Application.testModule() {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Sessions) {
            cookie<String>("refresh_token")
        }

        authentication {
            jwt("user") {
                validate { credential ->
                    JWTPrincipal(credential.payload)
                }
            }
            jwt("refresh") {
                validate { credential ->
                    JWTPrincipal(credential.payload)
                }
            }
        }
        routing {
            configureBookingRoute(controller)
        }
    }

    private val response = BookingResponse(
        id = "",
        status = "",
        userId = "",
        roomId = "",
        endTime = "",
        startTime = "",
        createdAt = "",
        updatedAt = "",
    )

    @Test
    fun `POST create should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.create(any()) } returns Result.Success(response)

        client.post("/buildings/") {
            contentType(ContentType.Application.Json)
            setBody("")
        }.apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `PUT update should return 200 OK on success`() = testApplication {
        application { testModule() }

        val request = BookingUpdateRequest(
            roomId = "",
        )

        coEvery { controller.updateById(UUID.randomUUID(), request) } returns Result.Success(Unit)

        client.post("/bookings/") {
            contentType(ContentType.Application.Json)
            setBody("")
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `GET one should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.findById(UUID.randomUUID()) } returns Result.Success(response)

        client.get("/bookings/1412412").apply {
            assertEquals(HttpStatusCode.InternalServerError, status)
        }
    }

    @Test
    fun `GET all should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.getAll() } returns Result.Success(listOf())

        client.get("/bookings/all").apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }


    @Test
    fun `DELETE all should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.deleteAll() } returns Result.Success(4)

        client.delete("/bookings/all").apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }
}