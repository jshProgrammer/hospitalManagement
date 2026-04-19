package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Nurse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NursesRepository : JpaRepository<Nurse, String> {
    override fun findAll(pageable: Pageable): Page<Nurse>
    fun findById(id: UUID): Optional<Nurse>
    fun findNurseByStationId(stationId: Long): List<Nurse>
}
