package org.hospitalmanagement.models.classes.facilities
import jakarta.persistence.*

@Entity
@Table(
    name = "station",
    indexes = [
        Index(name = "station_pkey", columnList = "id", unique = true),
        Index(name = "idx_station_department_id", columnList = "department")
    ]
)
class Station(
    @Id
    var id: Long,
    var name: String,
    @ManyToOne
    @JoinColumn(name = "department")
    var department: Department
)