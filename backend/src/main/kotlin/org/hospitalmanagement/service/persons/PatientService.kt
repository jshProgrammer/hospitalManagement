package org.hospitalmanagement.service.persons

import org.hospitalmanagement.dbRepositories.medication.DiagnosisRepository
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.models.classes.persons.Patient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PatientService(
    private val patientRepository: PatientRepository,
    private val diagnosisRepository: DiagnosisRepository,
) {
    fun getAll(pageable: Pageable): Page<Patient> =
        patientRepository.findAll(pageable)

    fun createWithNewPerson(patient: Patient): Patient =
        patientRepository.save(patient)
}