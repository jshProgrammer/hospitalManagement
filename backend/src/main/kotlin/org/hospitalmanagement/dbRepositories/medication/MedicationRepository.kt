package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.medication.Medication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface MedicationRepository : JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication> {

    @EntityGraph(attributePaths = ["dose", "drug"])
    override fun findAll(spec: Specification<Medication>?, pageable: Pageable): Page<Medication>
}