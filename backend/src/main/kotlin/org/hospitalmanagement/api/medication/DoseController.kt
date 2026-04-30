package org.hospitalmanagement.api.medication

import org.hospitalmanagement.models.classes.medication.Dose
import org.hospitalmanagement.models.enums.DoseUnit
import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.services.medication.DoseService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/doses")
class DoseController(private val doseService: DoseService) {

    @GetMapping
    fun getAll(
        pageable: Pageable,
        @RequestParam(required = false) unit: DoseUnit?,
        @RequestParam(required = false) frequency: DoseFrequency?,
        @RequestParam(required = false) amount: Long?,
        @RequestParam(required = false) frequencyAmount: Long?
    ): Page<Dose> =
        doseService.search(pageable, unit, frequency, amount, frequencyAmount)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Dose =
        doseService.findById(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Dose with id $id not found")


}