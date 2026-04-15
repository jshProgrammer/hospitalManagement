package org.hospitalmanagement.db.repositories

import org.hospitalmanagement.models.classes.medication.Medication
import org.springframework.stereotype.Repository
import java.util.*

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

import java.time.LocalDate
import java.util.Optional

@Repository
interface MedicationRepository: JpaRepository<Medication, String> {
    override fun findAll(): List<Medication>
    fun findById(id: Long): Optional<Medication>
}