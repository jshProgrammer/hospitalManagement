package org.hospitalmanagement.service.facilities

import org.hospitalmanagement.db.repositories.facilities.BookingsRepository
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.enums.BookingState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class BookingService(
    private val bookingsRepository: BookingsRepository
) {
    fun getAll(pageable: Pageable): Page<Booking> =
        bookingsRepository.findAll(pageable)

    fun getById(id: Long): Booking? =
        bookingsRepository.findById(id).orElse(null)

    fun getByState(state: BookingState): List<Booking> =
        bookingsRepository.findAllByState(state)

    fun create(booking: Booking): Booking =
        bookingsRepository.save(booking)

    /* TODO: to be implemented
    fun update(id: Long, updated: Booking): Booking? {
        val existing = bookingsRepository.findById(id).orElse(null) ?: return null

        val newBooking = existing.copy(
            // 👉 hier Felder anpassen je nach Booking-Klasse
            state = updated.state
            // z.B. patient = updated.patient,
            // date = updated.date
        )

        return bookingsRepository.save(newBooking)
    }
     */

    fun delete(id: Long) {
        bookingsRepository.deleteById(id)
    }
}