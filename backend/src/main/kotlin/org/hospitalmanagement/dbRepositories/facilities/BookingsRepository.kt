package org.hospitalmanagement.dbRepositories.facilities

import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.enums.BookingState
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable;

@Repository
interface BookingsRepository: JpaRepository<Booking, Long> {
    override fun findAll(pageable: Pageable): Page<Booking>
    fun findAllByState(state: BookingState): MutableList<Booking>
}