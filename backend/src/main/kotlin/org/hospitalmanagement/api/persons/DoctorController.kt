package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.persons.requestModels.*
import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.models.enums.Gender
import org.hospitalmanagement.service.persons.DoctorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/api/doctors")
class DoctorController(private val doctorService: DoctorService) {

    // Step 1: POST new person + employee data → returns matches or created doctor
    @PostMapping("/new")
    fun createDoctor(
        @RequestBody request: DoctorCreationRequest
    ): DoctorCreationResponse =
        doctorService.createDoctorWithSearch(
            PersonCreateRequest(
                gender = request.gender,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                phoneNumber = request.phoneNumber,
                plz = request.plz,
                city = request.city,
                street = request.street,
                houseNumber = request.houseNumber,
                country = request.country,
                birthday = request.birthday
            ),
            EmployeeCreationRequest(
                department = request.department,
                workPhone = request.workPhone,
                doctorType = request.doctorType
            )
        )

    // Step 2: POST existing personId → creates employee + doctor from existing person
    @PostMapping("/new/{personId}")
    fun createDoctorWithExisting(
        @PathVariable personId: UUID,
        @RequestBody employeeData: EmployeeCreationRequest
    ): DoctorRequest =
        doctorService.createDoctorWithExistingPerson(personId, employeeData)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID) =
        doctorService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Doctor with id $id not found"
            )

    @GetMapping()
    fun getDoctors(
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
        @RequestParam(required = false) streetNo: Int?,
        @RequestParam(required = false) type: DoctorsType?,
        @RequestParam(required = false) departmentId: Int?,
        @RequestParam(required = false) workphone: Int?,
    ): Page<Doctor> {
        return doctorService.searchDoctors(
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
            streetNo,
            type,
            departmentId,
            workphone
        )
    }
    //TODO: DIAGNOSED BY Doctor ->
}