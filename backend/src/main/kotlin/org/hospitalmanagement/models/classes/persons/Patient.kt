package org.hospitalmanagement.models.classes.persons

import jakarta.persistence.*

@Entity
@Table(name ="patient")
data class Patient(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @OneToOne
    @JoinColumn(name = "person", nullable = false)
    val person: Person
)
