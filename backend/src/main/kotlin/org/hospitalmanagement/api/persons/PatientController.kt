package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.facilities.requestModels.RelocateRequest
import org.hospitalmanagement.api.persons.requestModels.PatientCreationResponse
import org.hospitalmanagement.api.persons.requestModels.PatientRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.models.classes.facilities.Booking
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.Gender
import org.hospitalmanagement.service.facilities.BookingService
import org.hospitalmanagement.service.persons.PatientService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun createPatientWithExisting(@PathVariable personId: Long): PatientRequest =
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
        @RequestParam(required = false) after: Long?,
        @RequestParam(defaultValue = "10") limit: Int,
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
    ): ResponseEntity<Map<String, Any>> {
        val raw = patientService.searchPatientsPaginated(
            after, limit + 1,
            firstName, lastName, email, phone, gender,
            city, country, birthday, plz, street, streetNo
        )
        val hasMore = raw.size > limit
        val patients = if (hasMore) raw.dropLast(1) else raw
        return ResponseEntity.ok(mapOf(
            "patients" to patients,
            "nextCursor" to (patients.lastOrNull()?.id ?: 0),
            "hasMore" to hasMore
        ))
    }

    @GetMapping("/{id}/bookings")
    fun getBookingsById(
        @PathVariable id: Long,
        @RequestParam(required = false) after: Long?,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Map<String, Any>> {
        val raw = bookingService.getPatientBookingsPaginated(id, after, limit + 1)
        val hasMore = raw.size > limit
        val bookings = if (hasMore) raw.dropLast(1) else raw
        return ResponseEntity.ok(mapOf(
            "bookings" to bookings,
            "nextCursor" to (bookings.lastOrNull()?.id ?: 0),
            "hasMore" to hasMore
        ))
    }

    @PostMapping("/{id}/discharge")
    fun discharge(@PathVariable id: Long): Booking =
        bookingService.discharge(id)

    @PostMapping("/{id}/relocate")
    fun relocate(
        @PathVariable id: Long,
        @RequestBody request: RelocateRequest
    ): Booking =
        bookingService.relocate(id, request.roomId)

    @GetMapping("/{id}/diagnoses")
    fun getDiagnosesByID(
        @PathVariable id: Long,
        @RequestParam(required = false) after: Long?,
        @RequestParam(defaultValue = "10") limit: Int
    ): ResponseEntity<Map<String, Any>> {
        val raw = patientService.getDiagnosesPaginated(id, after, limit + 1)
        val hasMore = raw.size > limit
        val diagnoses = if (hasMore) raw.dropLast(1) else raw
        return ResponseEntity.ok(mapOf(
            "diagnoses" to diagnoses,
            "nextCursor" to (diagnoses.lastOrNull()?.id ?: 0),
            "hasMore" to hasMore
        ))
    }
}
