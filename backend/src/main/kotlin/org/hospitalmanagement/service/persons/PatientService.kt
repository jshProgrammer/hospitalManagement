package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.PatientCreationResponse
import org.hospitalmanagement.api.persons.requestModels.PatientRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.dbRepositories.persons.PersonRepository
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class PatientService(
    private val patientRepository: PatientRepository,
    private val personService: PersonService,
    private val personRepository: PersonRepository
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

    /*fun searchPatients(
        pageable: Pageable,
        firstName: String?,
        lastName: String?,
        email: String?,
        phone: String?,
        gender: Gender?,
        city: String?,
        country: String?,
        birthday: Date?,
        plz: Int,
        street: String?,
        streetNo: Int?): Page<Patient>{
        
    }*/

}