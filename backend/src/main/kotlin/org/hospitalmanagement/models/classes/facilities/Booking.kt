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
    val id: Long,

    @Temporal(TemporalType.DATE)
    val from: Date,

    @Temporal(TemporalType.DATE)
    val until: Date?,

    @Convert(converter = BookingState.BookingStateConverter::class)
    val state: BookingState,

    @OneToMany(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "room")
    val room: List<Room>,

    @ManyToOne
    @JoinColumn(name = "patient")
    val patient: Patient
)