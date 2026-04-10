package org.example.db

import org.example.db.repositories.EmployeeRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.PageRequest
import org.springframework.context.annotation.Bean

@SpringBootApplication
class HospitalApplication {

    @Bean
    fun run(employeeRepository: EmployeeRepository) = CommandLineRunner {
        // Entspricht deinem "ORDER BY id ASC LIMIT 100"
        val topEmployees = employeeRepository.findAllByOrderByIdAsc(PageRequest.of(0, 100))

        topEmployees.forEach { emp ->
            println("ID: ${emp.id}, Name: ${emp.person.firstName} ${emp.person.lastName}")
        }
    }
}

fun main(args: Array<String>) {
    runApplication<HospitalApplication>(*args)
}