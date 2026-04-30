package org.hospitalmanagement.service.facilities

import org.hospitalmanagement.dbRepositories.facilities.DepartmentRepository
import org.hospitalmanagement.dbRepositories.facilities.DepartmentSpecifications
import org.hospitalmanagement.models.classes.facilities.Department
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service


@Service
class DepartmentService(
    private val departmentRepository: DepartmentRepository
) {
    fun getById(id: Long): Department? =
        departmentRepository.findById(id).orElse(null)

    fun search(
        pageable: Pageable,
        name: String?,
        nameContains: String?,
        building: String?
    ): Page<Department> {

        if (name != null && nameContains != null) {
            throw IllegalArgumentException("Use either name or nameContains, not both")
        }

        var spec: Specification<Department>? = null

        if (name != null) {
            spec = Specification.where(DepartmentSpecifications.hasName(name))
        }

        if (nameContains != null) {
            spec = (spec ?: Specification.where(null))
                .and(DepartmentSpecifications.nameContains(nameContains))
        }

        if (building != null) {
            spec = (spec ?: Specification.where(null))
                .and(DepartmentSpecifications.hasBuilding(building))
        }

        return if (spec != null) {
            departmentRepository.findAll(spec, pageable)
        } else {
            departmentRepository.findAll(pageable)
        }
    }
}