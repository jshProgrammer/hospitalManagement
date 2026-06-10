package org.hospitalmanagement.specifications.medication

import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.enums.DrugsType
import org.hospitalmanagement.services.CryptoUtility
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.Date

@Component
class DiagnosisSpecification(private val cryptoUtility: CryptoUtility) {

    fun hasDisease(disease: String?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            if (disease == null) {
                cb.conjunction()   // = TRUE
            } else {
                val blindIndex = cryptoUtility.generateBlindIndex(disease)
                cb.equal(root.get<String>("diseaseHash"), blindIndex)
            }
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

    fun hasDiagnosedByDoctorId(doctorId: Long?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            doctorId?.let { cb.equal(root.get<Any>("diagnosedBy").get<Long>("id"), it) }
        }

    fun hasDiagnosedPatientId(patientId: Long?): Specification<Diagnosis> =
        Specification { root, _, cb ->
            patientId?.let { cb.equal(root.get<Any>("diagnosedPatient").get<Long>("id"), it) }
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
        medicationId: Long?,
        drugType: DrugsType?,
        diagnosedByDoctorId: Long?,
        diagnosedPatientId: Long?,
        diagnosedAfter: Date?,
        diagnosedBefore: Date?
    ): Specification<Diagnosis> =
        Specification.where(hasDisease(disease))
            .and(hasMedicationId(medicationId))
            .and(hasDrugType(drugType))
            .and(hasDiagnosedByDoctorId(diagnosedByDoctorId))
            .and(hasDiagnosedPatientId(diagnosedPatientId))
            .and(diagnosedAfter(diagnosedAfter))
            .and(diagnosedBefore(diagnosedBefore))

    fun afterId(id: Long): Specification<Diagnosis> =
        Specification { root, _, cb -> cb.greaterThan(root.get("id"), id) }
}
