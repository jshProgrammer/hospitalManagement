package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.PatientCreationResponse
import org.hospitalmanagement.api.persons.requestModels.PatientRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.dbRepositories.facilities.BookingsRepository
import org.hospitalmanagement.dbRepositories.medication.DiagnosisRepository
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.dbRepositories.persons.PatientSpecifications
import org.hospitalmanagement.dbRepositories.persons.PersonRepository
import org.hospitalmanagement.specifications.medication.DiagnosisSpecification
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.classes.medication.Medication
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.classes.persons.Person
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PatientService(
    private val patientRepository: PatientRepository,
    private val personService: PersonService,
    private val personRepository: PersonRepository,
    private val bookingsRepository: BookingsRepository,
    private val diagnosisRepository: DiagnosisRepository,
    private val patientSpecifications: PatientSpecifications
) {
    fun getAll(pageable: Pageable): Page<Patient> =
        patientRepository.findAll(pageable)

    fun getById(id: Long): Patient? =
        patientRepository.findById(id).orElse(null)

    fun getByFirstNameAndLastName(firstName: String, lastName: String): Optional<Patient> {
        val personOpt = personRepository.findPersonByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName)
        return if (personOpt.isPresent) {
            val person = personOpt.get()
            patientRepository.findByPersonId(person.id)
        } else {
            Optional.empty()
        }
    }

    fun createPatientWithSearch(personData: PersonCreateRequest): PatientCreationResponse {
        val potentialMatches = personService.findSimilarPersons(personData)

        return if (potentialMatches.isNotEmpty()) {
            PatientCreationResponse(potentialMatches = potentialMatches, createdPatient = null)
        } else {
            val newPerson = personService.createPerson(personData)
            val savedPatient = patientRepository.save(Patient(id = 0, person = newPerson))
            PatientCreationResponse(potentialMatches = null, createdPatient = toRequest(savedPatient))
        }
    }

    fun createPatientWithExistingPerson(personId: Long): PatientRequest {
        val person = personService.findById(personId)

        if (patientRepository.existsByPersonId(person.id!!)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Person $personId ist bereits ein Patient"
            )
        }

        val savedPatient = patientRepository.save(Patient(id = 0, person = person))
        return toRequest(savedPatient)
    }

    private fun toRequest(patient: Patient) = PatientRequest(
        id = patient.id,
        personId = patient.person.id!!
    )

    fun searchPatients(
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
        streetNo: Int?): Page<Patient>{
        var spec: Specification<Patient>? = null

        if (firstName != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasFirstName(firstName))
        }

        if (lastName != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasLastName(lastName))
        }

        if (email != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasEmail(email))
        }

        if (phone != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasPhone(phone))
        }

        if (gender != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasGender(gender))
        }

        if (city != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasCity(city))
        }

        if (country != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasCountry(country))
        }

        if (birthday != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasBirthday(birthday))
        }

        if (plz != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasPlz(plz))
        }

        if (street != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasStreet(street))
        }

        if (streetNo != null) {
            spec = (spec ?: Specification.where(null))
                .and(patientSpecifications.hasStreetNo(streetNo))
        }

        return if (spec != null) {
            patientRepository.findAll(spec, pageable)
        } else {
            patientRepository.findAll(pageable)
        }
    }

    fun searchPatientsPaginated(
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
        streetNo: Int?
    ): List<Patient> {
        var spec = Specification.where<Patient>(null)

        if (after != null) spec = spec.and(patientSpecifications.afterId(after))
        if (!firstName.isNullOrBlank()) spec = spec.and(patientSpecifications.hasFirstName(firstName))
        if (!lastName.isNullOrBlank()) spec = spec.and(patientSpecifications.hasLastName(lastName))
        if (!email.isNullOrBlank()) spec = spec.and(patientSpecifications.hasEmail(email))
        if (!phone.isNullOrBlank()) spec = spec.and(patientSpecifications.hasPhone(phone))
        if (gender != null) spec = spec.and(patientSpecifications.hasGender(gender))
        if (!city.isNullOrBlank()) spec = spec.and(patientSpecifications.hasCity(city))
        if (!country.isNullOrBlank()) spec = spec.and(patientSpecifications.hasCountry(country))
        if (birthday != null) spec = spec.and(patientSpecifications.hasBirthday(birthday))
        if (plz != null) spec = spec.and(patientSpecifications.hasPlz(plz))
        if (!street.isNullOrBlank()) spec = spec.and(patientSpecifications.hasStreet(street))
        if (streetNo != null) spec = spec.and(patientSpecifications.hasStreetNo(streetNo))

        val pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.asc("id")))
        return patientRepository.findAll(spec, pageable).content
    }

    fun getBookingsByPersonID(personID: Long, pageable: Pageable): Page<Booking> =
        bookingsRepository.findByPatientId(personID, pageable)

    fun getDiagnosesPaginated(patientID: Long, after: Long?, limit: Int): List<Diagnosis> {
        var spec = DiagnosisSpecification.hasDiagnosedPatientId(patientID)
        if (after != null) spec = spec.and(DiagnosisSpecification.afterId(after))
        val pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.asc("id")))
        return diagnosisRepository.findAll(spec, pageable).content
    }

    fun getDiagnoses(patientID: Long, pageable: Pageable): Page<Diagnosis> =
        diagnosisRepository.findByDiagnosedPatientId(patientID, pageable)
}
