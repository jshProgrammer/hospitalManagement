package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.db.Room
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.service.facilities.BookingService
import org.hospitalmanagement.services.RoomsService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/rooms")
class RoomsController(
    private val roomsService: RoomsService,
    private val bookingService: BookingService
) {
    // TODO: add filter by station + FILTER extra or as filter parameter?
    @GetMapping
    fun getAll(
        pageable: Pageable,
        @RequestParam(required = false) stationID: Long?,
        @RequestParam(required = false) number: Long?,
    ): Page<Room> =
        roomsService.search(
            pageable,
            stationID,
            number
        )

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Room =
        roomsService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Room with id $id not found"
            )

    @GetMapping("/floor/{floor}")
    fun getByFloor(@PathVariable floor: Long): List<Room> =
        roomsService.findByFloor(floor)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Room with id $floor not found"
            )

    @GetMapping("/{id}/bookings")
    fun getBookingsPerRoom(
        @PathVariable id: Long,
        pageable: Pageable
    ): Page<Booking> =
        bookingService.getByRoom(id, pageable)
}