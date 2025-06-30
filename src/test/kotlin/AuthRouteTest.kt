import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
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
import io.mockk.mockk
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.daazay.presentation.controller.auth.AuthController
import org.daazay.presentation.request.auth.AuthSignupRequest
import org.daazay.presentation.route.auth.configureAuthRoute
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.http.*
import io.ktor.http.auth.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.mockk.coEvery
import io.mockk.every
import kotlinx.datetime.LocalDateTime
import org.daazay.presentation.response.auth.AuthResponse
import org.daazay.presentation.response.auth.UserResponse
import org.daazay.utils.Result
import kotlin.time.Instant

class AuthRouteTest {
    private val controller = mockk<AuthController>()

    private val userResponse = UserResponse(
        id = "123e4567-e89b-12d3-a456-426614174000",
        username = "user",
        email = "test@example.com",
        role = "user",
        createdAt = "2024-01-01T00:00:00Z",
        updatedAt = "2024-06-01T00:00:00Z"
    )

    private val authResponse = AuthResponse(
        user = userResponse,
        accessToken = "access"
    )

    @BeforeTest
    fun setup() {
        clearAllMocks()
    }

    @Serializable
    data class TestResponse(val accessToken: String, val refreshToken: String)

    private fun Application.testModule() {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Sessions) {
            cookie<String>("refresh_token")
        }
        // Please read the jwt property from the config file if you are using EngineMain
        val jwtAudience = "jwt-audience"
        val jwtDomain = "https://jwt-provider-domain/"
        val jwtRealm = "ktor sample app"
        val jwtSecret = "secret"
        authentication {
            jwt("user") {
                realm = jwtRealm
                verifier(JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build())
                validate { credential -> JWTPrincipal(credential.payload) }
                challenge { _, _ ->
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token is not valid or has expired"))
                }
            }
            jwt("refresh") {
                realm = jwtRealm
                verifier(JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .build())
                validate { credential -> JWTPrincipal(credential.payload) }
                challenge { _, _ ->
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Token is not valid or has expired"))
                }
            }
        }
        routing {
            configureAuthRoute(controller)
        }
    }

    @Test
    fun `POST signup should return 200 OK on success`() = testApplication {
        application { testModule() }

        val request = AuthSignupRequest(
            username = "test",
            password = "password",
            email = "test@mail.com",
        )

        coEvery { controller.signup(request) } returns Result.Success(authResponse)

        client.post("/auth/signup") {
            contentType(ContentType.Application.Json)
            setBody(Json.encodeToString(request))
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
        }
    }
}

@Serializable
data class MySession(val count: Int = 0)