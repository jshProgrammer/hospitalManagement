package org.hospitalmanagement.models.classes.facilities

import org.hospitalmanagement.db.Room
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.BookingState
import java.util.Date
import jakarta.persistence.*

@Entity
@Table(name = "bookings")
class Booking(
    @Id
    @GeneratedValue
    val id: Long,

    @Temporal(TemporalType.DATE)
    @Column(name = "\"from\"")
    val from: Date,

    @Temporal(TemporalType.DATE)
    val until: Date?,

    @Convert(converter = BookingState.BookingStateConverter::class)
    val state: BookingState,

    @ManyToOne
    @JoinColumn(name = "room")
    val room: Room,

    @ManyToOne
    @JoinColumn(name = "patient")
    val patient: Patient
)