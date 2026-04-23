package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.persons.requestModels.*
import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.service.persons.DoctorService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

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

    @GetMapping
    fun getAll(pageable: org.springframework.data.domain.Pageable) =
        doctorService.getAll(pageable)
//TODO: AGAIN WITH PARAMETERS, YES
    @GetMapping("/type/{type}")
    fun getByType(@PathVariable type: String) =
        doctorService.getByType(enumValueOf<DoctorsType>(type).toString())

    //TODO: DIAGNOSED BY Doctor ->
    //TODO: GET BY NAME -> YES
    //TODO: GET BOOKING BY ROOM?
}