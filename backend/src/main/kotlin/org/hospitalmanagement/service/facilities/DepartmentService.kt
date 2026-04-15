package org.hospitalmanagement.service.facilities

import org.hospitalmanagement.db.repositories.facilities.DepartmentRepository
import org.hospitalmanagement.models.classes.facilities.Department
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository
) {
    fun getAll(pageable: Pageable): Page<Department> =
        departmentRepository.findAll(pageable)

    fun getById(id: Long): Department? =
        departmentRepository.findById(id).orElse(null)

    fun getByName(name: String): Department? =
        departmentRepository.findByName(name).orElse(null)

    fun getByNameContainingIgnoreCase(name: String): List<Department> =
        departmentRepository.findByNameContainingIgnoreCase(name)

    fun getByBuilding(building: String): List<Department> =
        departmentRepository.findByBuilding(building)
}