package org.hospitalmanagement.db.repositories

import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.enums.DoctorsType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface DoctorRepository : JpaRepository<Doctor, String> {
    //TODO: add other CRUD operations (specifically delete, update, add)
    override fun findAll(pageable: Pageable): Page<Doctor>
    fun findAllByEmployee_Department(department: Int): List<Doctor>
    fun findAllByEmployee_Department(department: Int, pageable: Pageable): Page<Doctor>
    fun findById(id: UUID): Optional<Doctor>
    //TODO: fun findByWork_phone(work_phone: String): Optional<Doctor>
    fun findByType(type: DoctorsType): List<Doctor>
}