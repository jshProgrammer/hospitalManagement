package org.hospitalmanagement.api.medication

import org.hospitalmanagement.api.medication.requestModels.MedicationRequest
import org.hospitalmanagement.models.classes.medication.Medication
import org.hospitalmanagement.models.enums.DoseUnit
import org.hospitalmanagement.models.enums.DrugsType
import org.hospitalmanagement.services.medication.MedicationService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.Date

@RestController
@RequestMapping("/api/medications")
class MedicationController(private val medicationService: MedicationService) {

    @GetMapping
    fun getAll(
        @RequestParam(required = false) after: Long?,
        @RequestParam(defaultValue = "10") limit: Int,
        @RequestParam(required = false) drugId: Long?,
        @RequestParam(required = false) drugType: DrugsType?,
        @RequestParam(required = false) doseUnit: DoseUnit?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startedAfter: Date?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startedBefore: Date?,
        @RequestParam(required = false) active: Boolean?
    ): ResponseEntity<Map<String, Any>> {
        val raw = medicationService.searchPaginated(
            after, limit + 1, drugId, drugType, doseUnit, startedAfter, startedBefore, active
        )
        val hasMore = raw.size > limit
        val medications = if (hasMore) raw.dropLast(1) else raw
        return ResponseEntity.ok(mapOf(
            "medications" to medications,
            "nextCursor" to (medications.lastOrNull()?.id ?: 0),
            "hasMore" to hasMore
        ))
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Medication =
        medicationService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Medication with id $id not found")

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: MedicationRequest): Medication =
        medicationService.create(request)

    @PostMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: MedicationRequest): Medication =
        medicationService.update(id, request)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Medication with id $id not found")

}