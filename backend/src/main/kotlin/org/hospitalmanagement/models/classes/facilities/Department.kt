package org.hospitalmanagement.models.classes.facilities

import jakarta.persistence.*

@Entity
@Table(name="department")
class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val name: String,

    val building: String
)
