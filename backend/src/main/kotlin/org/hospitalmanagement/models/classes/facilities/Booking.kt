package org.hospitalmanagement.models.classes.facilities

import org.hospitalmanagement.db.Room
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.BookingState
import java.util.Date

data class Booking(
    val id: Long,
    val from: Date,
    val until: Date?,
    val state: BookingState,
    val room: Room,
    val patient: Patient
)