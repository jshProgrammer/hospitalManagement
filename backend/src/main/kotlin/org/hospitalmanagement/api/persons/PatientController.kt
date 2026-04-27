package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.facilities.requestModels.RelocateRequest
import org.hospitalmanagement.api.persons.requestModels.PatientCreationResponse
import org.hospitalmanagement.api.persons.requestModels.PatientRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.classes.medication.Drug
import org.hospitalmanagement.models.classes.medication.Medication
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.Gender
import org.hospitalmanagement.service.facilities.BookingService
import org.hospitalmanagement.service.persons.PatientService
import org.hospitalmanagement.service.persons.PersonService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/patients")
class PatientController(
    private val patientService: PatientService,
    private val bookingService: BookingService
) {

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

    @GetMapping()
    fun getPatients(
        pageable: Pageable,
        @RequestParam(required = false) firstName: String?,
        @RequestParam(required = false) lastName: String?,
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) gender: Gender?,
        @RequestParam(required = false) city: String?,
        @RequestParam(required = false) country: String?,
        @RequestParam(required = false) birthday: Date?,
        @RequestParam(required = false) plz: Int?,
        @RequestParam(required = false) street: String?,
        @RequestParam(required = false) streetNo: Int?
        ): Page<Patient> {
        return patientService.searchPatients(
            pageable,
            firstName,
            lastName,
            email,
            phone,
            gender,
            city,
            country,
            birthday,
            plz,
            street,
            streetNo
        )
    }

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

    @GetMapping("/{id}/bookings")
    fun getBookingsById(@PathVariable id: Long, pageable: Pageable): Page<Booking> =
        patientService.getBookingsByPersonID(id, pageable)

    @PostMapping("/{id}/discharge")
    fun discharge(@PathVariable id: Long): Booking =
        bookingService.discharge(id)

    @PostMapping("/{id}/relocate")
    fun relocate(
        @PathVariable id: Long,
        @RequestBody request: RelocateRequest
    ): Booking =
        bookingService.relocate(id, request.roomId)

    /*
    // TODO: solve wrong DB dependency (Person instead of Patient)
    @GetMapping("/{id}/diagnoses")
    fun getDiagnosesByID(@PathVariable id: Long, pageable: Pageable): Page<Diagnosis> =
        patientService.getDiagnoses(id, pageable)
     */


    // TODO: does "get medication per patient ID" really make sense
    //  => medication is part of diagnoses that can be returned with method above
    /*@GetMapping("/{id}/medication")
    fun getMedicationByID(@PathVariable id: Long, pageable: Pageable): Page<Medication> =
        patientService.getMedicationsByPersonID(id)
     */
    /*
    @GetMapping("/{id}/drugs")
    fun getDrugsByID(@PathVariable id: Long): Page<Drug> =
        patientService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Patient with id $id not found"
            )
    */
}