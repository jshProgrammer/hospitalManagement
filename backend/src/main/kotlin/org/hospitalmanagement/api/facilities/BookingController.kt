package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.api.facilities.requestModels.BookingRequest
import org.hospitalmanagement.api.facilities.requestModels.RelocateRequest
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.enums.BookingState
import org.hospitalmanagement.service.facilities.BookingService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService
) {

    @GetMapping
    fun getAll(
        @RequestParam(required = false) after: Long?,
        @RequestParam(defaultValue = "10") limit: Int,
        @RequestParam(required = false) state: BookingState?
    ): ResponseEntity<Map<String, Any>> {
        val raw = bookingService.searchPaginated(after, limit + 1, state)
        val hasMore = raw.size > limit
        val bookings = if (hasMore) raw.dropLast(1) else raw
        return ResponseEntity.ok(mapOf(
            "bookings" to bookings,
            "nextCursor" to (bookings.lastOrNull()?.id ?: 0),
            "hasMore" to hasMore
        ))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Booking =
        bookingService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Booking with id $id not found"
            )

    @PostMapping
    fun create(@RequestBody bookingRequest: BookingRequest): Booking =
        bookingService.create(bookingRequest)

    @PostMapping("/{id}/relocate")
    fun relocate(@PathVariable id: Long, @RequestBody request: RelocateRequest): Booking =
        bookingService.relocateBooking(id, request.roomId)

    @PostMapping("/{id}/complete")
    fun complete(@PathVariable id: Long): Booking =
        bookingService.completeBooking(id)

    /*
    // not yet necessary as discharge and relocate are separate POST-calls
    @PatchMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: BookingUpdateRequest
    ): Booking =
        bookingService.update(id, request)
     */
}
