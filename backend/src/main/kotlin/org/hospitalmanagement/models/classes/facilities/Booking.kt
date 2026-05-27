package org.hospitalmanagement.models.classes.facilities

import org.hospitalmanagement.db.Room
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.BookingState
import java.util.Date
import jakarta.persistence.*

@Entity
@Table(
    name = "bookings",
    indexes = [
        Index(name = "bookings_pkey", columnList = "id", unique = true),
        Index(name = "idx_bookings_from_id", columnList = "\"from\", id"),
        Index(name = "idx_bookings_patient_from_id", columnList = "patient, \"from\", id"),
        Index(name = "idx_bookings_room_from_until", columnList = "room, \"from\", until"),
        Index(name = "idx_bookings_state_from_id", columnList = "state, \"from\", id")
    ]
)
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

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