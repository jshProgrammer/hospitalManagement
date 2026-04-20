package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.persons.requestModels.*
import org.hospitalmanagement.service.persons.NurseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/api/nurses")
class NurseController(private val nurseService: NurseService) {

    // Step 1: POST new person + employee data → returns matches or created nurse
    @PostMapping("/new")
    fun createNurse(
        @RequestBody request: NurseCreationRequest
    ): NurseCreationResponse =
        nurseService.createNurseWithSearch(
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

                stationId = request.stationId
            )
        )

    // Step 2: POST existing personId → creates employee + nurse from existing person
    @PostMapping("/new/{personId}")
    fun createNurseWithExisting(
        @PathVariable personId: UUID,
        @RequestBody employeeData: EmployeeCreationRequest
    ): NurseRequest =
        nurseService.createNurseWithExistingPerson(personId, employeeData)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID) =
        nurseService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Nurse with id $id not found"
            )

    @GetMapping
    fun getAll(pageable: org.springframework.data.domain.Pageable) =
        nurseService.getAll(pageable)

    @GetMapping("/station/{stationId}")
    fun getByStationId(@PathVariable stationId: Long) =
        nurseService.getByStationId(stationId)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Nurses for station with id $stationId not found"
            )

    //TODO: GET BY NAME + Combination Filters??
}