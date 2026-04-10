package org.hospitalmanagement.db.repositories

import org.hospitalmanagement.models.classes.persons.Employee
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, String> {
    // Spring versteht diesen Methodennamen und generiert das SQL automatisch!
    fun findAllByOrderByIdAsc(pageable: Pageable): List<Employee>
}