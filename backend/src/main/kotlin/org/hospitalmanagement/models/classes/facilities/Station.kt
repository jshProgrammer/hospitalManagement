package org.hospitalmanagement.models.classes.facilities
import jakarta.persistence.*

@Entity
@Table(name = "station")
class Station(
    @Id
    var id: Long,
    var name: String,
    @ManyToOne
    @JoinColumn(name = "department")
    var department: Department
)