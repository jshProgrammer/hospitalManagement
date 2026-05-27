package org.hospitalmanagement.dbRepositories.facilities

import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.enums.BookingState
import org.springframework.data.jpa.domain.Specification

object BookingSpecification {

    fun hasState(state: BookingState): Specification<Booking> =
        Specification { root, _, cb -> cb.equal(root.get<BookingState>("state"), state) }

    fun hasPatientId(patientId: Long): Specification<Booking> =
        Specification { root, _, cb -> cb.equal(root.get<Any>("patient").get<Long>("id"), patientId) }

    fun afterId(id: Long): Specification<Booking> =
        Specification { root, _, cb -> cb.greaterThan(root.get("id"), id) }
}
