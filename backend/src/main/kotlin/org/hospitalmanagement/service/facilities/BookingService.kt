package org.hospitalmanagement.service.facilities

import org.hospitalmanagement.api.facilities.requestModels.BookingRequest
import org.hospitalmanagement.dbRepositories.facilities.BookingsRepository
import org.hospitalmanagement.dbRepositories.facilities.RoomsRepository
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.enums.BookingState
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date


@Service
class BookingService(
    private val bookingsRepository: BookingsRepository,
    private val roomRepository: RoomsRepository,
    private val patientRepository: PatientRepository,
) {
    fun getAll(pageable: Pageable): Page<Booking> =
        bookingsRepository.findAll(pageable)

    fun getById(id: Long): Booking? =
        bookingsRepository.findById(id).orElse(null)

    fun getByState(state: BookingState): List<Booking> =
        bookingsRepository.findAllByState(state)

    fun create(request: BookingRequest): Booking {
        val room = roomRepository.findById(request.roomId)
            .orElseThrow { RuntimeException("Room not found") }

        val patient = patientRepository.findById(request.patientId)
            .orElseThrow { RuntimeException("Patient not found") }

        val booking = Booking(
            from = request.from,
            until = request.until,
            state = request.state,
            room = room,
            patient = patient
        )

        return bookingsRepository.save(booking)
    }

    fun getByRoom(roomId: Long, pageable: Pageable): Page<Booking> =
        bookingsRepository.findByRoomId(roomId, pageable)

    fun discharge(patientId: Long): Booking {
        val today = LocalDate.now()
        val date = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant())

        return update(patientId, date, BookingState.COMPLETED)
    }

    fun relocate(patientId: Long, roomId: Long): Booking {
        val today = LocalDate.now()
        val date = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant())

        // close old booking
        update(patientId, date, BookingState.RELOCATED)

        // create new booking
        return create(BookingRequest(
            date,
            null,
            BookingState.CONFIRMED,
            roomId,
            patientId,
        ))
    }

    private fun update(id: Long, until: Date?, state: BookingState?): Booking {
        // TODO: is it ensured that we really have only one ongoing booking in the DB?
        val existing = bookingsRepository.findFirstByPatientIdAndStateIn(
            id,
            listOf(BookingState.CONFIRMED, BookingState.CHECKED_IN)
        )
            .orElseThrow { RuntimeException("No checked-in or confirmed booking with patient id $id found") }

        val updated = existing.copy(
            until = until ?: existing.until,
            state = state ?: existing.state
        )

        return bookingsRepository.save(updated)
    }

}