package org.hospitalmanagement.dbRepositories.medication

import org.springframework.data.domain.Page
import org.hospitalmanagement.models.classes.medication.Dose
import org.hospitalmanagement.models.enums.DoseUnit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable;

@Repository
interface DoseRepository: JpaRepository<Dose, Long> {
    override fun findAll(pageable: Pageable): Page<Dose>
    fun findByUnit(doseUnit: DoseUnit): MutableList<Dose>
}