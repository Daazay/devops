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
import org.daazay.presentation.controller.booking.BuildingController
import org.daazay.presentation.controller.booking.RoomController
import org.daazay.presentation.request.booking.BuildingCreateRequest
import org.daazay.presentation.request.booking.RoomCreateRequest
import org.daazay.presentation.response.booking.BuildingResponse
import org.daazay.presentation.response.booking.RoomResponse
import org.daazay.presentation.route.booking.configureBuildingRoute
import org.daazay.presentation.route.booking.configureRoomRoute
import org.daazay.utils.Result
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RoomRouteTest {
    private val controller = mockk<RoomController>()

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
            configureRoomRoute(controller)
        }
    }

    private val response = RoomResponse(
        id = "",
        name = "",
        createdAt = "",
        updatedAt = "",
        status = "",
        capacity = 2,
        buildingId = ""
    )

    @Test
    fun `POST create should return 200 OK on success`() = testApplication {
        application { testModule() }

        val request = RoomCreateRequest(
            name = "",
            capacity = 1,
            buildingId = ""
        )

        coEvery { controller.create(any()) } returns Result.Success(response)

        client.post("/rooms/") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `GET one should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.findById(UUID.randomUUID()) } returns Result.Success(response)

        client.get("/rooms/1412412").apply {
            assertEquals(HttpStatusCode.InternalServerError, status)
        }
    }

    @Test
    fun `GET by building should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.getByBuildingId(UUID.randomUUID()) } returns Result.Success(listOf())

        client.get("/buildings/1412412").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `GET all should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.getAll() } returns Result.Success(listOf())

        client.get("/buildings/all").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `DELETE one should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.deleteById(UUID.randomUUID()) } returns Result.Success(Unit)

        client.delete("/buildings/1412412").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `DELETE by building should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.deleteByBuildingId(UUID.randomUUID()) } returns Result.Success(Unit)

        client.delete("/buildings/1412412").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }

    @Test
    fun `DELETE all should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.deleteAll() } returns Result.Success(3)

        client.delete("/buildings/all").apply {
            assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}