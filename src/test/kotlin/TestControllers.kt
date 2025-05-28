import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.daazay.domain.model.building.*
import org.daazay.domain.service.building.BookingService
import org.daazay.domain.service.building.BuildingService
import org.daazay.domain.service.building.RoomService
import org.daazay.presentation.controller.booking.BookingController
import org.daazay.presentation.controller.booking.BuildingController
import org.daazay.presentation.controller.booking.RoomController
import org.daazay.presentation.request.booking.BookingCreateRequest
import org.daazay.presentation.request.booking.BuildingCreateRequest
import org.daazay.presentation.request.booking.RoomCreateRequest
import org.daazay.presentation.response.booking.RoomResponse
import org.daazay.utils.Result
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import java.util.*
import kotlin.test.assertTrue

class TestControllers {
    private val buildingService = mockk<BuildingService>()
    private val buildingController = BuildingController(buildingService)

    private val roomService = mockk<RoomService>()
    private val roomController = RoomController(roomService)

    private val bookingService = mockk<BookingService>()
    private val bookingController = BookingController(bookingService)

    @Test
    fun `create building returns success`() = runBlocking {
        val request = BuildingCreateRequest(
            "itmo corp 2",
            "nowhere"
        )
        val building = Building(
            UUID.randomUUID(),
            "itmo corp 2",
            "nowhere",
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        )

        coEvery { buildingService.create(request) } returns building

        val result = buildingController.create(request)

        assertTrue(result is Result.Success)
        assertEquals(request.name, result.data!!.name)
    }

    @Test
    fun `create room returns success`() = runBlocking {
        val request = RoomCreateRequest(
            "6979edc4-2c07-44cc-a726-238e6ba3b3f2",
            "room 2",
            124,
        )
        val room = Room(
            UUID.fromString("6979edc4-2c07-44cc-a726-238e6ba3b3f2"),
            UUID.randomUUID(),
            name = "room 2",
            capacity = 145,
            status = RoomStatus.OCCUPIED,
            createdAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
            updatedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
        )

        coEvery { roomService.create(request) } returns room

        val result = roomController.create(request)

        assertTrue(result is Result.Success)
        assertEquals(request.name, result.data!!.name)
    }

    @Test
    fun `create booking returns success`() = runBlocking {
        val request = BookingCreateRequest(
            userId = "6979edc4-2c07-44cc-a726-238e6ba3b3f2",
            roomId = "4a8726e0-1fda-4333-acea-c06553045bdb",
            "12.12.12 12:12:12",
            "12.12.12 12:12:12",
        )
        val booking = Booking(
            UUID.randomUUID(),
            userId = UUID.fromString("6979edc4-2c07-44cc-a726-238e6ba3b3f2"),
            roomId = UUID.fromString( "4a8726e0-1fda-4333-acea-c06553045bdb"),
            Clock.System.now().toLocalDateTime(TimeZone.UTC),
            Clock.System.now().toLocalDateTime(TimeZone.UTC),
            BookingStatus.CONFIRMED,
            Clock.System.now().toLocalDateTime(TimeZone.UTC),
            Clock.System.now().toLocalDateTime(TimeZone.UTC),
        )

        coEvery { bookingService.create(request) } returns booking

        val result = bookingController.create(request)

        assertTrue(result is Result.Success)
        assertEquals(request.roomId, result.data!!.roomId)
    }
}