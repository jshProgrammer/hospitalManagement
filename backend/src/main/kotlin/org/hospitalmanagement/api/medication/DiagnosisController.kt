package org.hospitalmanagement.api.medication

import org.hospitalmanagement.api.medication.requestModels.DiagnosisRequest
import org.hospitalmanagement.models.enums.DrugsType
import org.hospitalmanagement.services.medication.DiagnosisService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@RestController
@RequestMapping("/api/diagnoses")
class DiagnosisController(private val diagnosisService: DiagnosisService) {

    @GetMapping
    fun getAll(
        @RequestParam(required = false) after: Long?,
        @RequestParam(defaultValue = "10") limit: Int,
        @RequestParam(required = false) disease: String?,
        @RequestParam(required = false) diseaseContains: String?,
        @RequestParam(required = false) medicationId: Long?,
        @RequestParam(required = false) drugType: DrugsType?,
        @RequestParam(required = false) diagnosedByDoctorId: Long?,
        @RequestParam(required = false) diagnosedPatientId: Long?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) diagnosedAfter: Date?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) diagnosedBefore: Date?
    ): ResponseEntity<Map<String, Any>> {
        val raw = diagnosisService.searchPaginated(
            after, limit + 1, disease, diseaseContains, medicationId, drugType,
            diagnosedByDoctorId, diagnosedPatientId, diagnosedAfter, diagnosedBefore
        )
        val hasMore = raw.size > limit
        val diagnoses = if (hasMore) raw.dropLast(1) else raw
        return ResponseEntity.ok(mapOf(
            "diagnoses" to diagnoses,
            "nextCursor" to (diagnoses.lastOrNull()?.id ?: 0),
            "hasMore" to hasMore
        ))
    }

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
