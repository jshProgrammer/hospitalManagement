package org.hospitalmanagement.services.medication

import org.hospitalmanagement.api.medication.requestModels.DiagnosisRequest
import org.hospitalmanagement.dbRepositories.medication.DiagnosisRepository
import org.hospitalmanagement.dbRepositories.medication.MedicationRepository
import org.hospitalmanagement.dbRepositories.persons.DoctorRepository
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.enums.DrugsType
import org.hospitalmanagement.specifications.medication.DiagnosisSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.UUID

@Service
class DiagnosisService(
    private val diagnosisRepository: DiagnosisRepository,
    private val medicationRepository: MedicationRepository,
    private val doctorRepository: DoctorRepository,
    private val patientRepository: PatientRepository
) {

    fun search(
        pageable: Pageable,
        disease: String?,
        diseaseContains: String?,
        medicationId: Long?,
        drugType: DrugsType?,
        diagnosedByDoctorId: UUID?,
        diagnosedPatientId: UUID?,
        diagnosedAfter: Date?,
        diagnosedBefore: Date?
    ): Page<Diagnosis> {
        val spec = DiagnosisSpecification.build(
            disease, diseaseContains, medicationId, drugType,
            diagnosedByDoctorId, diagnosedPatientId, diagnosedAfter, diagnosedBefore
        )
        return diagnosisRepository.findAll(spec, pageable)
    }

    fun findById(id: Long): Diagnosis? =
        diagnosisRepository.findById(id).orElse(null)

    fun create(request: DiagnosisRequest): Diagnosis {
        val medication = medicationRepository.findById(request.medicationId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Medication with id ${request.medicationId} not found") }
        val doctor = doctorRepository.findById(request.diagnosedBy)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor with id ${request.diagnosedBy} not found") }
        val patient = patientRepository.findById(request.diagnosedPatient)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Patient with id ${request.diagnosedPatient} not found") }

        return diagnosisRepository.save(
            Diagnosis(
                id = 0,
                disease = request.disease,
                medication = medication,
                diagnosedBy = doctor,
                diagnosedPatient = patient,
                diagnosedAt = request.diagnosedAt,
                null
            )
        )
    }

    fun update(id: Long, request: DiagnosisRequest): Diagnosis? {
        if (!diagnosisRepository.existsById(id)) return null

        val medication = medicationRepository.findById(request.medicationId)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Medication with id ${request.medicationId} not found"
                )
            }
        val doctor = doctorRepository.findById(request.diagnosedBy)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Doctor with id ${request.diagnosedBy} not found"
                )
            }
        val patient = patientRepository.findById(request.diagnosedPatient)
            .orElseThrow {
                ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Patient with id ${request.diagnosedPatient} not found"
                )
            }

        return diagnosisRepository.save(
            Diagnosis(
                id = id,
                disease = request.disease,
                medication = medication,
                diagnosedBy = doctor,
                diagnosedPatient = patient,
                diagnosedAt = request.diagnosedAt,
                null
            )
        )
    }

    fun terminate(id: Long): Diagnosis? {
        if (!diagnosisRepository.existsById(id)) return null

        val diagnosis: Diagnosis = diagnosisRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnosis with id $id not found") }

        val now = LocalDateTime.now()
        val date = Date.from(now.atZone(ZoneId.systemDefault()).toInstant())

        return diagnosisRepository.save(
            Diagnosis(
                id,
                diagnosis.disease,
                diagnosis.medication,
                diagnosis.diagnosedBy,
                diagnosis.diagnosedPatient,
                diagnosis.diagnosedAt,
                date
            )
        )
    }
}