package org.hospitalmanagement.models.classes.persons

import jakarta.persistence.*

@Entity
@Table(
    name = "employee",
    indexes = [
        Index(name = "employee_pkey", columnList = "id", unique = true),
        Index(name = "uq_employee_person", columnList = "person", unique = true),
        Index(name = "idx_employee_department_id", columnList = "department")
    ]
)
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
