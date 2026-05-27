package org.hospitalmanagement.models.classes.persons

import jakarta.persistence.*

@Entity
@Table(
    name = "patient",
    indexes = [
        Index(name = "patient_pkey", columnList = "id", unique = true),
        Index(name = "uq_patient_person", columnList = "person", unique = true)
    ]
)
data class Patient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @OneToOne
    @JoinColumn(name = "person", nullable = false)
    val person: Person
)
