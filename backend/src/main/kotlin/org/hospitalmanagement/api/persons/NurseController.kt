package org.hospitalmanagement.api.persons

import org.hospitalmanagement.api.persons.requestModels.*
import org.hospitalmanagement.models.classes.persons.Nurse
import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.Gender
import org.hospitalmanagement.service.persons.NurseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

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
                workPhone = null,
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

    @GetMapping("/station/{stationId}")
    fun getByStationId(@PathVariable stationId: Long) =
        nurseService.getByStationId(stationId)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Nurses for station with id $stationId not found"
            )

    @GetMapping()
    fun getNurses(
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
        @RequestParam(required = false) stationId: Int?,
        @RequestParam(required = false) departmentId: Long?,
    ): Page<Nurse> {
        return nurseService.searchNurses(
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
            stationId,
            departmentId
        )
    }
}
