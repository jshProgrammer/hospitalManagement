package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.PatientCreationResponse
import org.hospitalmanagement.api.persons.requestModels.PatientRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.dbRepositories.facilities.BookingsRepository
import org.hospitalmanagement.dbRepositories.medication.DiagnosisRepository
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.dbRepositories.persons.PersonRepository
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.classes.medication.Medication
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.classes.persons.Person
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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
    private val diagnosisRepository: DiagnosisRepository
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

    fun createPatientWithExistingPerson(personId: UUID): PatientRequest {
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
        birthday: Date?,
        plz: Int?,
        street: String?,
        streetNo: Int?): Page<Patient>{
        var spec: Specification<Patient>? = null

        if (firstName != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasFirstName(firstName))
        }

        if (lastName != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasLastName(lastName))
        }

        if (email != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasEmail(email))
        }

        if (phone != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasPhone(phone))
        }

        if (gender != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasGender(gender))
        }

        if (city != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasCity(city))
        }

        if (country != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasCountry(country))
        }

        if (birthday != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasBirthday(birthday))
        }

        if (plz != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasPlz(plz))
        }

        if (street != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasStreet(street))
        }

        if (streetNo != null) {
            spec = (spec ?: Specification.where(null))
                .and(PatientSpecifications.hasStreetNo(streetNo))
        }

        return if (spec != null) {
            patientRepository.findAll(spec, pageable)
        } else {
            patientRepository.findAll(pageable)
        }
    }

    fun getBookingsByPersonID(personID: Long, pageable: Pageable): Page<Booking> =
        bookingsRepository.findByPatientId(personID, pageable)


    fun getDiagnoses(patientID: Long, pageable: Pageable): Page<Diagnosis> =
        diagnosisRepository.findByDiagnosedPatientId(patientID, pageable)
}