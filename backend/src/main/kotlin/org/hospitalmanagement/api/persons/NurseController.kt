package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.persons.requestModels.EmployeeCreationRequest
import org.hospitalmanagement.api.persons.requestModels.NurseCreationResponse
import org.hospitalmanagement.api.persons.requestModels.NurseRequest
import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
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
        @RequestBody personData: PersonCreateRequest,
        @RequestParam department: Int,
        @RequestParam stationId: Long
    ): NurseCreationResponse =
        nurseService.createNurseWithSearch(
            personData,
            EmployeeCreationRequest(department = department, workPhone = "", stationId = stationId)
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