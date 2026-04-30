package org.hospitalmanagement.dbRepositories.facilities

import org.hospitalmanagement.models.classes.facilities.Department
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface DepartmentRepository : JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    override fun findById(id: Long): Optional<Department>
}