package org.hospitalmanagement.db.repositories.facilities

import org.hospitalmanagement.models.classes.facilities.Department
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DepartmentRepository : JpaRepository<Department, String> {
    //TODO: add other CRUD operations (update, add)
    override fun findAll(pageable: Pageable): Page<Department>
    fun findById(id: Long): Optional<Department>
    fun findByName(name: String): Optional<Department>
    fun findByNameContainingIgnoreCase(name: String): List<Department>
    fun findByBuilding(building: String): List<Department>
}