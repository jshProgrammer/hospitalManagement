package org.hospitalmanagement.db.repositories.persons

import org.hospitalmanagement.models.classes.persons.Patient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface PatientRepository : JpaRepository<Patient, String> {
    //TODO: add other CRUD operations (specifically delete, update, add)
    override fun findAll(pageable: Pageable): Page<Patient>
    fun findById(id: Long): Optional<Patient> // uses the patient id, NOT the person id
}