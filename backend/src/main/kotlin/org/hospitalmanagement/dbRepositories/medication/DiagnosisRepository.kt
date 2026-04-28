package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.classes.medication.Medication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface DiagnosisRepository : JpaRepository<Diagnosis, Long>, JpaSpecificationExecutor<Diagnosis> {
    fun findByDiagnosedPatientId(diagnosedPatientID: Long, pageable: Pageable): Page<Diagnosis>
}