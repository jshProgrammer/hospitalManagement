package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.EmployeeCreationRequest
import org.hospitalmanagement.api.persons.requestModels.NurseCreationResponse
import org.hospitalmanagement.api.persons.requestModels.NurseRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.dbRepositories.facilities.StationRepository
import org.hospitalmanagement.dbRepositories.persons.EmployeeRepository
import org.hospitalmanagement.dbRepositories.persons.NurseSpecifications
import org.hospitalmanagement.dbRepositories.persons.NursesRepository
import org.hospitalmanagement.models.classes.persons.Employee
import org.hospitalmanagement.models.classes.persons.Nurse
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

@Service
class NurseService(
    private val nursesRepository: NursesRepository,
    private val employeeRepository: EmployeeRepository,
    private val stationRepository: StationRepository,
    private val personService: PersonService
) {
    fun getAll(pageable: Pageable): Page<Nurse> =
        nursesRepository.findAll(pageable)

    fun getById(id: UUID): Nurse? =
        nursesRepository.findById(id).orElse(null)

    fun getByStationId(stationId: Long): List<Nurse> =
        nursesRepository.findNurseByStationId(stationId)
    // Step 1: Search for existing persons, or create new person + employee + nurse directly
    fun createNurseWithSearch(
        personData: PersonCreateRequest,
        employeeData: EmployeeCreationRequest
    ): NurseCreationResponse {
        val potentialMatches = personService.findSimilarPersons(personData)

        return if (potentialMatches.isNotEmpty()) {
            NurseCreationResponse(potentialMatches = potentialMatches, createdNurse = null)
        } else {
            val newPerson = personService.createPerson(personData)
            val nurse = createEmployeeAndNurse(newPerson.id?.let {
                personService.findById(it)
            } ?: newPerson, employeeData)
            NurseCreationResponse(potentialMatches = null, createdNurse = toRequest(nurse))
        }
    }

    // Step 2a: Use existing person to create employee + nurse
    fun createNurseWithExistingPerson(personId: UUID, employeeData: EmployeeCreationRequest): NurseRequest {
        val person = personService.findById(personId)
        val nurse = createEmployeeAndNurse(person, employeeData)
        return toRequest(nurse)
    }

    private fun createEmployeeAndNurse(
        person: org.hospitalmanagement.models.classes.persons.Person,
        employeeData: EmployeeCreationRequest
    ): Nurse {
        val station = stationRepository.findById(employeeData.stationId!!)
            .orElseThrow { IllegalArgumentException("Station nicht gefunden: ${employeeData.stationId}") }
        val employee = employeeRepository.save(Employee(person = person, department = employeeData.department))
        return nursesRepository.save(Nurse(id = employee.id!!, station = station))
    }

    private fun toRequest(nurse: Nurse) = NurseRequest(
        id = nurse.id,
        stationId = nurse.station.id
    )
    fun searchNurses(
        pageable: Pageable,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?,
        gender: Gender?,
        city: String?,
        country: String?,
        birthday: Date?,
        plz: Int?,
        street: String?,
        streetNo: Int?, 
        stationId: Int?,
        departmentId: Int?,
        ): Page<Nurse>{
        var spec: Specification<Nurse>? = null

        if (firstName != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasFirstName(firstName))
        }

        if (lastName != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasLastName(lastName))
        }

        if (email != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasEmail(email))
        }

        if (phone != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasPhone(phone))
        }

        if (gender != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasGender(gender))
        }

        if (city != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasCity(city))
        }

        if (country != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasCountry(country))
        }

        if (birthday != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasBirthday(birthday))
        }

        if (plz != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasPlz(plz))
        }

        if (street != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasStreet(street))
        }

        if (streetNo != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasStreetNo(streetNo))
        }

        if (departmentId != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasDepartmentId(departmentId))
        }

        if (stationId != null) {
            spec = (spec ?: Specification.where(null))
                .and(NurseSpecifications.hasStationId(stationId))
        }

        return if (spec != null) {
            nursesRepository.findAll(spec, pageable)
        } else {
            nursesRepository.findAll(pageable)
        }
    }
}