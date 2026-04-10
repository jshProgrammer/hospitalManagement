package org.hospitalmanagement

import org.hospitalmanagement.db.repositories.DepartmentRepository
import org.hospitalmanagement.db.repositories.DoctorRepository
import org.hospitalmanagement.db.repositories.PatientRepository
import org.hospitalmanagement.models.enums.DoctorsType
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.util.Optionals.ifPresentOrElse
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

    /*
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
     */

    // MARK: Department Operations

    /*
    // "SELECT * FROM DEPARTMENT ORDER BY id ASC LIMIT 100"
    @Bean
    fun run(departmentRepository: DepartmentRepository) = CommandLineRunner {
        val topDepartments = departmentRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "id")))

        topDepartments.forEach { dep ->
            println("ID: ${dep.id}, Name: ${dep.name} ${dep.building}")
        }
    }
     */

    /*
    // "SELECT * FROM DEPARTMENT WHERE id = 1"
    @Bean
    fun run(departmentRepository: DepartmentRepository) = CommandLineRunner {
        val dep = departmentRepository.findById(1)
            .ifPresentOrElse(
                { dep ->
                    println("ID: ${dep.id}, Name: ${dep.name} ${dep.building}")
                },
                {
                    println("Department nicht gefunden")
                }
            )
    }
    */

    /*
    // "SELECT * FROM DEPARTMENT WHERE NAME = .."
    @Bean
    fun run(departmentRepository: DepartmentRepository) = CommandLineRunner {
        val dep = departmentRepository.findByName("Klinik und Poliklinik für Anästhesiologie, Intensivmedizin, Notfallmedizin und Schmerztherapie")
            .ifPresentOrElse(
                { dep ->
                    println("ID: ${dep.id}, Name: ${dep.name} ${dep.building}")
                },
                {
                    println("Department nicht gefunden")
                }
            )
    }
     */

    /*
    // "SELECT * FROM DEPARTMENT WHERE NAME CONTAINS ..."
    @Bean
    fun run(departmentRepository: DepartmentRepository) = CommandLineRunner {
        val dep = departmentRepository.findByNameContainingIgnoreCase("Zahn")
            .forEach { dep ->
                println("ID: ${dep.id}, Name: ${dep.name} ${dep.building}")
            }
    }
     */

    /*
    // "SELECT * FROM DEPARTMENT WHERE building = ..."
    @Bean
    fun run(departmentRepository: DepartmentRepository) = CommandLineRunner {
        val dep = departmentRepository.findByBuilding("1A")
            .forEach { dep ->
                println("ID: ${dep.id}, Name: ${dep.name} ${dep.building}")
            }
    }
     */

    // MARK: Doctor Operations

    /*
    // "SELECT * FROM DOCTORS ORDER BY id ASC LIMIT 100"
    @Bean
    fun run(doctorRepository: DoctorRepository) = CommandLineRunner {
        val topDoctors = doctorRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Direction.ASC, "id")))

        topDoctors.forEach { doc ->
            println("ID: ${doc.id}, Name: ${doc.employee.person.firstName} ${doc.employee.person.lastName}, Work phone: ${doc.work_phone}, Type: ${doc.type}")
        }
    }
     */

    /*
    // "SELECT * FROM DOCTORS WHERE ID = ..."
    @Bean
    fun run(doctorRepository: DoctorRepository) = CommandLineRunner {
        val doctor = doctorRepository.findById(UUID.fromString("a2ce7587-510f-4e2c-93c7-74f41ce6d9cb"))
            .ifPresentOrElse(
                { doc ->
                    println("ID: ${doc.id}, Name: ${doc.employee.person.firstName} ${doc.employee.person.lastName}, Work phone: ${doc.work_phone}, Type: ${doc.type}")
                },
                {
                    println("Doktor nicht gefunden")
                }
            )
    }
     */

    // "SELECT * FROM DOCTORS WHERE employee.department = ..."
    @Bean
    fun run(doctorRepository: DoctorRepository) = CommandLineRunner {
        val doctor = doctorRepository.findAllByEmployee_Department(20)
            .forEach { doc ->
                println("ID: ${doc.id}, Name: ${doc.employee.person.firstName} ${doc.employee.person.lastName}, Work phone: ${doc.work_phone}, Type: ${doc.type}")
            }
    }

    /*
    // "SELECT * FROM DOCTORS WHERE Type = ..."
    @Bean
    fun run(doctorRepository: DoctorRepository) = CommandLineRunner {
        val doctors = doctorRepository.findByType(DoctorsType.CHIEF_PHYSICIAN)

        doctors.forEach { doc ->
            println("ID: ${doc.id}, Name: ${doc.employee.person.firstName} ${doc.employee.person.lastName}, Work phone: ${doc.work_phone}, Type: ${doc.type}")
        }
    }
     */
}

fun main(args: Array<String>) {
    runApplication<HospitalApplication>(*args)
}