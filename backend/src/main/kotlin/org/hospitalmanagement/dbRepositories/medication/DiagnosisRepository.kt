package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface DiagnosisRepository : JpaRepository<Diagnosis, Long>, JpaSpecificationExecutor<Diagnosis> {

    fun findByDiagnosedPatientId(diagnosedPatientID: Long, pageable: Pageable): Page<Diagnosis>

    @EntityGraph(attributePaths = [
        "medication", "medication.dose", "medication.drug",
        "diagnosedBy", "diagnosedBy.employee", "diagnosedBy.employee.person",
        "diagnosedPatient", "diagnosedPatient.person"
    ])
    override fun findAll(spec: Specification<Diagnosis>?, pageable: Pageable): Page<Diagnosis>
}
