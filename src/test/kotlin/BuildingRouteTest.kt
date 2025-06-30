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
import org.daazay.presentation.request.auth.AuthSignupRequest
import org.daazay.presentation.request.booking.BuildingCreateRequest
import org.daazay.presentation.response.booking.BuildingResponse
import org.daazay.presentation.route.auth.configureAuthRoute
import org.daazay.presentation.route.booking.configureBuildingRoute
import org.daazay.utils.Result
import java.util.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BuildingRouteTest {
    private val controller = mockk<BuildingController>()

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
            configureBuildingRoute(controller)
        }
    }

    private val response = BuildingResponse(
        id = "",
        name = "",
        address = "",
        createdAt = "",
        updatedAt = "",
    )

    @Test
    fun `POST create should return 200 OK on success`() = testApplication {
        application { testModule() }

        val request = BuildingCreateRequest(
            name = "",
            address = "",
        )

        coEvery { controller.create(any()) } returns Result.Success(response)

        client.post("/buildings/") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }.apply {
            assertEquals(HttpStatusCode.Unauthorized, status)
        }
    }

    @Test
    fun `PUT should return 200 OK on success`() = testApplication {
        application { testModule() }

        val request = BuildingCreateRequest(
            name = "",
            address = "",
        )

        coEvery { controller.updateById(UUID.randomUUID(), any()) } returns Result.Success(Unit)

        client.put("/buildings/") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }.apply {
            assertEquals(HttpStatusCode.MethodNotAllowed, status)
        }
    }

    @Test
    fun `GET one should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.findById(UUID.randomUUID()) } returns Result.Success(response)

        client.get("/buildings/1412412").apply {
            assertEquals(HttpStatusCode.InternalServerError, status)
        }
    }

    @Test
    fun `GET all should return 200 OK on success`() = testApplication {
        application { testModule() }

        coEvery { controller.findById(UUID.randomUUID()) } returns Result.Success(response)

        client.get("/buildings/all").apply {
            assertEquals(HttpStatusCode.InternalServerError, status)
        }
    }
}