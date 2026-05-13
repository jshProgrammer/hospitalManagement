package org.hospitalmanagement.models.classes.persons

import jakarta.persistence.*

@Entity
@Table(name = "employee")
class Employee(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "person", nullable = false)
    val person: Person,

    // could be connected to Department-object, but probably unnecessary (network traffic)
    val department: Long
)
