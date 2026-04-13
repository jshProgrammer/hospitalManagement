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

    // TODO: probably connect to Department-object instead
    val department: Int
)
