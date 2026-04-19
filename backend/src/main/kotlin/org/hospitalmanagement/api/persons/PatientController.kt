package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.persons.requestModels.PatientCreationResponse
import org.hospitalmanagement.api.persons.requestModels.PatientRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.service.persons.PatientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/api/patients")
class PatientController(private val patientService: PatientService) {

    // Step 1: POST new person data → returns matches or created patient
    @PostMapping("/new")
    fun createPatient(@RequestBody personData: PersonCreateRequest): PatientCreationResponse =
        patientService.createPatientWithSearch(personData)

    // Step 2: POST existing personId → creates patient from existing person
    @PostMapping("/new/{personId}")
    fun createPatientWithExisting(@PathVariable personId: UUID): PatientRequest =
        patientService.createPatientWithExistingPerson(personId)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Patient =
        patientService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Patient with id $id not found"
            )

    @GetMapping
    fun getAll(): Page<Patient> =
        patientService.getAll(Pageable.unpaged())

    @GetMapping("/{firstName}/{lastName}")
    fun getByFirstNameAndLastName(
        @PathVariable firstName: String,
        @PathVariable lastName: String
    ): Patient =
        patientService.getByFirstNameAndLastName(firstName, lastName)
             .orElseThrow {
                 ResponseStatusException(
                     HttpStatus.NOT_FOUND,
                     "Patient with name $firstName $lastName not found"
                 )
             }

}