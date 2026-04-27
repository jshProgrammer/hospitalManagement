package org.hospitalmanagement.dbRepositories.facilities

import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.enums.BookingState
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable;
import java.util.Optional

@Repository
interface BookingsRepository: JpaRepository<Booking, Long> {
    override fun findAll(pageable: Pageable): Page<Booking>
    fun findAllByState(state: BookingState): MutableList<Booking>
    fun findByRoomId(roomId: Long, pageable: Pageable): Page<Booking>
    fun findByPatientId(patientId: Long, pageable: Pageable): Page<Booking>
    fun findFirstByPatientIdAndStateIn(
        patientId: Long,
        states: List<BookingState>
    ): Optional<Booking>
}