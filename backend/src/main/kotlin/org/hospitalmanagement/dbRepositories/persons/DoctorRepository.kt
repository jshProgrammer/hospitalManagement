package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Nurse
import org.hospitalmanagement.models.enums.DoctorsType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DoctorRepository : JpaRepository<Doctor, Long>, JpaSpecificationExecutor<Doctor> {
    override fun findAll(pageable: Pageable): Page<Doctor>
    fun findAllByEmployee_Department(department: Long): List<Doctor>
    fun findAllByEmployee_Department(department: Long, pageable: Pageable): Page<Doctor>
    override fun findById(id: Long): Optional<Doctor>
    fun findByType(type: DoctorsType): List<Doctor>
}
