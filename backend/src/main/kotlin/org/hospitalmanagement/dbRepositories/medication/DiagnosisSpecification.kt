package org.hospitalmanagement.specifications.medication

import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.enums.DrugsType
import org.springframework.data.jpa.domain.Specification
import java.util.Date
import java.util.UUID

object DiagnosisSpecification {

    fun hasDisease(disease: String?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            disease?.let { cb.equal(cb.lower(root.get("disease")), it.lowercase()) }
        }

    fun diseaseContains(disease: String?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            disease?.let { cb.like(cb.lower(root.get("disease")), "%${it.lowercase()}%") }
        }

    fun hasMedicationId(medicationId: Long?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            medicationId?.let { cb.equal(root.get<Any>("medication").get<Long>("id"), it) }
        }

    fun hasDrugType(drugType: DrugsType?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            drugType?.let {
                cb.equal(
                    root.get<Any>("medication").get<Any>("drug").get<DrugsType>("type"), it
                )
            }
        }

    fun hasDiagnosedByDoctorId(doctorId: UUID?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            doctorId?.let { cb.equal(root.get<Any>("diagnosedBy").get<UUID>("id"), it) }
        }

    fun hasDiagnosedPatientId(patientId: UUID?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            patientId?.let { cb.equal(root.get<Any>("diagnosedPatient").get<UUID>("id"), it) }
        }

    fun diagnosedAfter(date: Date?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            date?.let { cb.greaterThanOrEqualTo(root.get("diagnosedAt"), it) }
        }

    fun diagnosedBefore(date: Date?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            date?.let { cb.lessThanOrEqualTo(root.get("diagnosedAt"), it) }
        }

    fun build(
        disease: String?,
        diseaseContains: String?,
        medicationId: Long?,
        drugType: DrugsType?,
        diagnosedByDoctorId: UUID?,
        diagnosedPatientId: UUID?,
        diagnosedAfter: Date?,
        diagnosedBefore: Date?
    ): Specification<Diagnosis> =
        Specification.where(hasDisease(disease))
            .and(diseaseContains(diseaseContains))
            .and(hasMedicationId(medicationId))
            .and(hasDrugType(drugType))
            .and(hasDiagnosedByDoctorId(diagnosedByDoctorId))
            .and(hasDiagnosedPatientId(diagnosedPatientId))
            .and(diagnosedAfter(diagnosedAfter))
            .and(diagnosedBefore(diagnosedBefore))
}

