package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Nurse
import org.hospitalmanagement.models.classes.persons.Patient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface NursesRepository : JpaRepository<Nurse, Long>, JpaSpecificationExecutor<Nurse> {
    @EntityGraph(attributePaths = ["employee", "employee.person", "station", "station.department"])
    override fun findAll(pageable: Pageable): Page<Nurse>

    @EntityGraph(attributePaths = ["employee", "employee.person", "station", "station.department"])
    override fun findAll(spec: Specification<Nurse>?, pageable: Pageable): Page<Nurse>

    override fun findById(id: Long): Optional<Nurse>
    fun findNurseByStationId(stationId: Long): List<Nurse>
}
