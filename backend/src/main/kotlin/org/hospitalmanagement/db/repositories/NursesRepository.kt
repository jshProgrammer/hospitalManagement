package org.hospitalmanagement.db.repositories

import org.hospitalmanagement.models.classes.persons.Nurse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NursesRepository: JpaRepository<Nurse, String> {
    override fun findAll(): List<Nurse>
    fun findById(id: UUID): Optional<Nurse>
    fun findNurseByStation(station: Nurse): List<Nurse>

}