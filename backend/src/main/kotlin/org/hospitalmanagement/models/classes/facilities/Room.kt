package org.hospitalmanagement.db

import jakarta.persistence.Entity
import org.hospitalmanagement.models.classes.facilities.Station
import jakarta.persistence.*

@Entity
@Table(name = "rooms")
class Room(
    @Id
    val id: Long,

    @ManyToOne
    @JoinColumn(name = "station")
    val station: Station,

    val number: Long,

    val floor: Long,

    val beds: Long
)