package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.DoctorCreationResponse
import org.hospitalmanagement.api.persons.requestModels.DoctorRequest
import org.hospitalmanagement.api.persons.requestModels.EmployeeCreationRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.dbRepositories.persons.DoctorRepository
import org.hospitalmanagement.dbRepositories.persons.EmployeeRepository
import org.hospitalmanagement.dbRepositories.persons.DoctorSpecifications
import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Employee
import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.models.enums.Gender
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

@Service
class DoctorService(
    private val doctorRepository: DoctorRepository,
    private val employeeRepository: EmployeeRepository,
    private val personService: PersonService,
    private val doctorSpecifications: DoctorSpecifications
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
                employeeData.workPhone?.let { Doctor(employee = employee, workPhone = it, type = employeeData.doctorType!!) }
            )
            DoctorCreationResponse(potentialMatches = null, createdDoctor = toRequest(doctor))
        }
    }

    // Step 2a: Use existing person to create employee + doctor
    fun createDoctorWithExistingPerson(personId: Long, employeeData: EmployeeCreationRequest): DoctorRequest {
        val person = personService.findById(personId)
        val employee = employeeRepository.save(Employee(person = person, department = employeeData.department))
        val doctor = doctorRepository.save(
            employeeData.workPhone?.let { Doctor(employee = employee, workPhone = it, type = employeeData.doctorType!!) }
        )
        return toRequest(doctor)
    }

    private fun toRequest(doctor: Doctor) = DoctorRequest(
        id = doctor.id!!,
        employeeId = doctor.employee.id!!,
        personId = doctor.employee.person.id!!,
        workPhone = doctor.workPhone,
        type = doctor.type,
        department = doctor.employee.department
    )

    fun getAll(pageable: Pageable): Page<Doctor> =
        doctorRepository.findAll(pageable)

    @Cacheable("doctors")
    fun getById(id: Long): Doctor? =
        doctorRepository.findById(id).orElse(null)

    fun getByType(type: String): List<Doctor> =
        doctorRepository.findByType(enumValueOf(type))

    fun searchDoctors(
        pageable: Pageable,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?,
        gender: Gender?,
        city: String?,
        country: String?,
        birthday: String?,
        plz: String?,
        street: String?,
        streetNo: Int?,
        Type: DoctorsType?,
        departmentId: Long?,
        workphone: String?,
    ): Page<Doctor>{
        var spec: Specification<Doctor>? = null

        if (firstName != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasFirstName(firstName))
        }

        if (lastName != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasLastName(lastName))
        }

        if (email != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasEmail(email))
        }

        if (phone != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasPhone(phone))
        }

        if (gender != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasGender(gender))
        }

        if (city != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasCity(city))
        }

        if (country != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasCountry(country))
        }

        if (birthday != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasBirthday(birthday))
        }

        if (plz != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasPlz(plz))
        }

        if (street != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasStreet(street))
        }

        if (streetNo != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasStreetNo(streetNo))
        }

        if (departmentId != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasDepartmentId(departmentId))
        }

        if (Type != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasType(Type))
        }

        if (workphone != null) {
            spec = (spec ?: Specification.where(null))
                .and(doctorSpecifications.hasWorkphone(workphone))
        }

        return if (spec != null) {
            doctorRepository.findAll(spec, pageable)
        } else {
            doctorRepository.findAll(pageable)
        }
    }

    fun searchDoctorsPaginated(
        after: Long?,
        limit: Int,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?,
        gender: Gender?,
        city: String?,
        country: String?,
        birthday: String?,
        plz: String?,
        street: String?,
        streetNo: Int?,
        type: DoctorsType?,
        departmentId: Long?,
        workPhone: String?
    ): List<Doctor> {
        var spec = Specification.where<Doctor>(null)

        if (after != null) spec = spec.and(doctorSpecifications.afterId(after))
        if (!firstName.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasFirstName(firstName))
        if (!lastName.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasLastName(lastName))
        if (!email.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasEmail(email))
        if (!phone.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasPhone(phone))
        if (gender != null) spec = spec.and(doctorSpecifications.hasGender(gender))
        if (!city.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasCity(city))
        if (!country.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasCountry(country))
        if (birthday != null) spec = spec.and(doctorSpecifications.hasBirthday(birthday))
        if (plz != null) spec = spec.and(doctorSpecifications.hasPlz(plz))
        if (!street.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasStreet(street))
        if (streetNo != null) spec = spec.and(doctorSpecifications.hasStreetNo(streetNo))
        if (type != null) spec = spec.and(doctorSpecifications.hasType(type))
        if (departmentId != null) spec = spec.and(doctorSpecifications.hasDepartmentId(departmentId))
        if (!workPhone.isNullOrBlank()) spec = spec.and(doctorSpecifications.hasWorkphone(workPhone))

        val pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.asc("id")))
        return doctorRepository.findAll(spec, pageable).content
    }
}
