package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.*
import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.classes.persons.Person
import java.util.Date
@Entity
@Table(name = "diagnosis")
class Diagnosis(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val disease: String,

    @ManyToOne
    @JoinColumn(name = "medication")
    val medication: Medication,

    @ManyToOne
    @JoinColumn(name = "diagnosed_by")
    val diagnosedBy: Doctor,

    @ManyToOne
    @JoinColumn(name = "diagnosed_patient")
    val diagnosedPatient: Patient,

    val diagnosedAt: Date
)