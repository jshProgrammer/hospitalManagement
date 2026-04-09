package org.example.models.classes.facilities

import BookingState
import org.example.models.classes.persons.Patient
import java.util.Date

data class Booking(
    val id: Long,
    val from: Date,
    val until: Date?,
    val state: BookingState,
    val room: Room,
    val patient: Patient
)