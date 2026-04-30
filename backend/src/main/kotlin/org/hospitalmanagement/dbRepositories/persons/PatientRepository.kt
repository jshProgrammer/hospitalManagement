package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Patient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PatientRepository : JpaRepository<Patient, String>, JpaSpecificationExecutor<Patient> {
    override fun findAll(pageable: Pageable): Page<Patient>
    fun findById(id: Long): Optional<Patient> // uses the patient id, NOT the person id
    fun existsByPersonId(personId: UUID): Boolean
    fun findByPersonId(personId: UUID?): Optional<Patient>
}