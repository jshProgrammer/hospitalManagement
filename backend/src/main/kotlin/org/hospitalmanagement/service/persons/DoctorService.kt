package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.DoctorCreationResponse
import org.hospitalmanagement.api.persons.requestModels.DoctorRequest
import org.hospitalmanagement.api.persons.requestModels.EmployeeCreationRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.dbRepositories.persons.DoctorRepository
import org.hospitalmanagement.dbRepositories.persons.EmployeeRepository
import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Employee
import org.hospitalmanagement.models.classes.persons.Nurse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class DoctorService(
    private val doctorRepository: DoctorRepository,
    private val employeeRepository: EmployeeRepository,
    private val personService: PersonService
) {
    // Step 1: Search for existing persons, or create new person + employee + doctor directly
    fun createDoctorWithSearch(
        personData: PersonCreateRequest,
        employeeData: EmployeeCreationRequest
    ): DoctorCreationResponse {
        val potentialMatches = personService.findSimilarPersons(personData)

        return if (potentialMatches.isNotEmpty()) {
            DoctorCreationResponse(potentialMatches = potentialMatches, createdDoctor = null)
        } else {
            val newPerson = personService.createPerson(personData)
            val employee = employeeRepository.save(Employee(person = newPerson, department = employeeData.department))
            val doctor = doctorRepository.save(
                employeeData.workPhone?.let { Doctor(employee = employee, work_phone = it, type = employeeData.doctorType!!) }
            )
            DoctorCreationResponse(potentialMatches = null, createdDoctor = toRequest(doctor))
        }
    }

    // Step 2a: Use existing person to create employee + doctor
    fun createDoctorWithExistingPerson(personId: UUID, employeeData: EmployeeCreationRequest): DoctorRequest {
        val person = personService.findById(personId)
        val employee = employeeRepository.save(Employee(person = person, department = employeeData.department))
        val doctor = doctorRepository.save(
            employeeData.workPhone?.let { Doctor(employee = employee, work_phone = it, type = employeeData.doctorType!!) }
        )
        return toRequest(doctor)
    }

    private fun toRequest(doctor: Doctor) = DoctorRequest(
        id = doctor.id!!,
        employeeId = doctor.employee.id!!,
        personId = doctor.employee.person.id!!,
        workPhone = doctor.work_phone,
        type = doctor.type,
        department = doctor.employee.department
    )

    fun getAll(pageable: Pageable): Page<Doctor> =
        doctorRepository.findAll(pageable)

    fun getById(id: UUID): Doctor? =
        doctorRepository.findById(id).orElse(null)

    fun getByType(type: String): List<Doctor> =
        doctorRepository.findByType(enumValueOf(type))
}