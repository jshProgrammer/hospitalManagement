package org.hospitalmanagement.db.repositories

import org.hospitalmanagement.models.classes.medication.Drug
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable;

@Repository
interface DrugsRepository: JpaRepository<Drug, Long> {
    override fun findAll(pageable: Pageable): Page<Drug>
    fun findByName(name: String): Drug?
}