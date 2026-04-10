package org.hospitalmanagement.models.classes.medication

import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Patient
import java.util.Date

data class Diagnosis(
    val id: Long,
    val medication: Long,
    val disease: String,
    val diagnosedBy: Doctor,
    val diagnosedPatient: Patient,
    val diagnosedAt: Date
)