package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.*
import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.classes.persons.Person
import java.util.Date
import java.util.Optional

@Entity
@Table(
    name = "diagnosis",
    indexes = [
        Index(name = "diagnosis_pkey", columnList = "id", unique = true),
        Index(name = "idx_diagnosis_date_id", columnList = "diagnosed_at, id"),
        Index(name = "idx_diagnosis_disease_date", columnList = "disease, diagnosed_at"),
        Index(name = "idx_diagnosis_doctor_date_id", columnList = "diagnosed_by, diagnosed_at, id"),
        Index(name = "idx_diagnosis_medication", columnList = "medication"),
        Index(name = "idx_diagnosis_patient_date_id", columnList = "diagnosed_patient, diagnosed_at, id")
    ]
)
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

    @Temporal(TemporalType.DATE)
    @Column(name = "diagnosed_at")
    val diagnosedAt: Date,


    @Transient
    val diagnosedEnd: Date?
)
