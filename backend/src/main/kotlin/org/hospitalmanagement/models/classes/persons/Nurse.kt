package org.hospitalmanagement.models.classes.persons

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hospitalmanagement.models.classes.facilities.Station
import jakarta.persistence.*

@Entity
@Table(name = "nurses")
class Nurse (
    @Id
    val id: Long? = null,

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    val employee: Employee,

    @ManyToOne
    @JoinColumn(name = "station")
    val station: Station,
)
