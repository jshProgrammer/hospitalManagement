package org.example.models.classes.medication

import org.example.models.classes.persons.Doctor
import org.example.models.classes.persons.Patient
import java.util.Date

data class Diagnosis(
    val id: Long,
    val medication: Long,
    val disease: String,
    val diagnosedBy: Doctor,
    val diagnosedPatient: Patient,
    val diagnosedAt: Date
)