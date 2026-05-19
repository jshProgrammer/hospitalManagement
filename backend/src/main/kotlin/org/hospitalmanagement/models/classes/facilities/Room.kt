package org.hospitalmanagement.db

import jakarta.persistence.Entity
import org.hospitalmanagement.models.classes.facilities.Station
import jakarta.persistence.*

@Entity
@Table(
    name = "rooms",
    indexes = [
        Index(name = "rooms_pkey", columnList = "id", unique = true),
        Index(name = "idx_rooms_station_number", columnList = "station, number"),
        Index(name = "idx_rooms_station_id", columnList = "station, id")
    ]
)
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