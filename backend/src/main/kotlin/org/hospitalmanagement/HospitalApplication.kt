package org.hospitalmanagement

import org.hospitalmanagement.db.repositories.EmployeeRepository
import org.hospitalmanagement.db.repositories.PatientRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.domain.PageRequest
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.Sort
import java.util.Arrays.sort
import java.util.UUID

@SpringBootApplication
class HospitalApplication {

    // MARK: Employee Operations

    /*
    // "SELECT * FROM EMPLOYEE ORDER BY id ASC LIMIT 100"
    @Bean
    fun run(employeeRepository: EmployeeRepository) = CommandLineRunner {
        val topEmployees = employeeRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "id")))

        topEmployees.forEach { emp ->
            println("ID: ${emp.id}, Name: ${emp.person.firstName} ${emp.person.lastName}")
        }
    }
     */

    /*
    // "SELECT * FROM EMPLOYEE WHERE department = 20"
    @Bean
    fun run(employeeRepository: EmployeeRepository) = CommandLineRunner {
        val employees = employeeRepository.findAllByDepartment(20)

        employees.forEach { emp ->
            println("ID: ${emp.id}, Name: ${emp.person.firstName} ${emp.person.lastName}")
        }
    }
     */

    /*
    // "SELECT * FROM EMPLOYEE WHERE id = ae390908-184f-4a76-9ea2-08cd79de9569"
    @Bean
    fun run(employeeRepository: EmployeeRepository) = CommandLineRunner {
        val emp = employeeRepository.findById(UUID.fromString("ae390908-184f-4a76-9ea2-08cd79de9569"))
            .ifPresentOrElse(
                { emp ->
                    println("Name: ${emp.person.firstName} ${emp.person.lastName}")
                },
                {
                    println("Employee nicht gefunden")
                }
            )
    }
     */


    // MARK: Patient Operations

    /*
    // "SELECT * FROM PATIENT ORDER BY id ASC LIMIT 100"
    @Bean
    fun run(patientRepository: PatientRepository) = CommandLineRunner {
        val topPatients = patientRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "id")))

        topPatients.forEach { pat ->
            println("ID: ${pat.id}, Name: ${pat.person.firstName} ${pat.person.lastName}")
        }
    }
     */


    // "SELECT * FROM PATIENT WHERE id = ae390908-184f-4a76-9ea2-08cd79de9569"
    @Bean
    fun run(patientRepository: PatientRepository) = CommandLineRunner {
        val pat = patientRepository.findById(1)
            .ifPresentOrElse(
                { pat ->
                    println("Name: ${pat.person.firstName} ${pat.person.lastName}")
                },
                {
                    println("Patient nicht gefunden")
                }
            )
    }

}

fun main(args: Array<String>) {
    runApplication<HospitalApplication>(*args)
}