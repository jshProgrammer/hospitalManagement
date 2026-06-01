package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.*
import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.classes.persons.Person
import org.hospitalmanagement.config.converters.EncryptedStringConverter
import java.util.Date
import java.util.Optional

@Entity
@Table(
    name = "diagnosis",
    indexes = [
        Index(name = "diagnosis_pkey", columnList = "id", unique = true),
        Index(name = "idx_diagnosis_date_id", columnList = "diagnosed_at, id"),
        Index(name = "idx_diagnosis_disease_hash", columnList = "disease_hash"),
        Index(name = "idx_diagnosis_doctor_date_id", columnList = "diagnosed_by, diagnosed_at, id"),
        Index(name = "idx_diagnosis_medication", columnList = "medication"),
        Index(name = "idx_diagnosis_patient_date_id", columnList = "diagnosed_patient, diagnosed_at, id")
    ]
)
class Diagnosis(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "disease_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    val disease: String,

    @Column(name = "disease_hash")
    val diseaseHash: String = "",

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

