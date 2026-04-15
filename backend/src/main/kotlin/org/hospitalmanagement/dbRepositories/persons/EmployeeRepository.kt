package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Employee
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface EmployeeRepository : JpaRepository<Employee, String> {
    //TODO: add other CRUD operations (specifically delete, update, add)
    override fun findAll(pageable: Pageable): Page<Employee>
    fun findAllByDepartment(department: Int): List<Employee>
    fun findAllByDepartment(department: Int, pageable: Pageable): Page<Employee>
    fun findById(id: UUID): Optional<Employee> // uses the employee id, NOT the person id
}