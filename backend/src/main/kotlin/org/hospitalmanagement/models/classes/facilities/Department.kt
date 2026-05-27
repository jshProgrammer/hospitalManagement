package org.hospitalmanagement.models.classes.facilities

import jakarta.persistence.*

@Entity
@Table(
    name = "department",
    indexes = [
        Index(name = "department_pkey", columnList = "id", unique = true),
    ]
)
class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val name: String,

    val building: String
)
