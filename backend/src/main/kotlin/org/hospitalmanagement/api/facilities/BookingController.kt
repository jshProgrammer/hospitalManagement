package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.api.facilities.requestModels.BookingRequest
import org.hospitalmanagement.api.facilities.requestModels.RelocateRequest
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
    fun create(@RequestBody bookingRequest: BookingRequest): Booking =
        bookingService.create(bookingRequest)

    // TODO: is it clear that the patient id is meant here? perhaps use request body instead or put it in PatientController?
    // TODO: discharge and relocating take booking id currently! change as soon as discussed in team
    @PostMapping("/{id}/discharge")
    fun discharge(@PathVariable id: Long): Booking =
        bookingService.discharge(id)

    @PostMapping("/{id}/relocate")
    fun relocate(
        @PathVariable id: Long,
        @RequestBody request: RelocateRequest
    ): Booking =
        bookingService.relocate(id, request.roomId)

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