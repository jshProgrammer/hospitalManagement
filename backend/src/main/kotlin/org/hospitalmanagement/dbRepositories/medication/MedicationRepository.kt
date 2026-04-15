package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.medication.Medication
import org.springframework.stereotype.Repository

import org.springframework.data.jpa.repository.JpaRepository

import java.util.Optional

@Repository
interface MedicationRepository: JpaRepository<Medication, String> {
    override fun findAll(): List<Medication>
    fun findById(id: Long): Optional<Medication>
}