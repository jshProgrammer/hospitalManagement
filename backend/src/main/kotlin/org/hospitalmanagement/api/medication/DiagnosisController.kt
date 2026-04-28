package org.hospitalmanagement.api.medication

import org.hospitalmanagement.api.medication.requestModels.DiagnosisRequest
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.enums.DrugsType
import org.hospitalmanagement.services.medication.DiagnosisService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.Date
import java.util.UUID

@RestController
@RequestMapping("/api/diagnoses")
class DiagnosisController(private val diagnosisService: DiagnosisService) {

    @GetMapping
    fun getAll(
        pageable: Pageable,
        @RequestParam(required = false) disease: String?,
        @RequestParam(required = false) diseaseContains: String?,
        @RequestParam(required = false) medicationId: Long?,
        @RequestParam(required = false) drugType: DrugsType?,
        @RequestParam(required = false) diagnosedByDoctorId: UUID?,
        @RequestParam(required = false) diagnosedPatientId: UUID?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) diagnosedAfter: Date?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) diagnosedBefore: Date?
    ): Page<Diagnosis> =
        diagnosisService.search(
            pageable, disease, diseaseContains, medicationId, drugType,
            diagnosedByDoctorId, diagnosedPatientId, diagnosedAfter, diagnosedBefore
        )

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Diagnosis =
        diagnosisService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnosis with id $id not found")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: DiagnosisRequest): Diagnosis =
        diagnosisService.create(request)

    // might not be used
    @PostMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: DiagnosisRequest): Diagnosis =
        diagnosisService.update(id, request)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnosis with id $id not found")

    @PostMapping("/{id}/terminate")
    fun terminate(@PathVariable id: Long): Diagnosis {
        return diagnosisService.terminate(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Diagnosis with id $id not found")
    }
}