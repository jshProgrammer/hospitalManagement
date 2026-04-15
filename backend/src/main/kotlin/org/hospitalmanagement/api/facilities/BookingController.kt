package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.enums.BookingState
import org.hospitalmanagement.service.facilities.BookingService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/bookings")
class BookingController(
    private val bookingService: BookingService
) {

    @GetMapping
    fun getAll(
        pageable: Pageable,
        @RequestParam(required = false) state: BookingState?
    ): Any {
        return if (state != null) {
            bookingService.getByState(state)
        } else {
            bookingService.getAll(pageable)
        }
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Booking =
        bookingService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Booking with id $id not found"
            )

    @PostMapping
    fun create(@RequestBody booking: Booking): Booking =
        bookingService.create(booking)

    /* TODO
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody booking: Booking
    ): Booking? =
        bookingService.update(id, booking)
     */
}