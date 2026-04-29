package org.hospitalmanagement.models.classes.persons

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "employee")
class Employee(
    @Id
    @GeneratedValue
    val id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "person", nullable = false)
    val person: Person,

    // could be connected to Department-object, but probably unnecessary (network traffic)
    val department: Long
)
