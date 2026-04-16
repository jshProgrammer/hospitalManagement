package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.models.classes.medication.Drug
import org.hospitalmanagement.services.facilities.DrugService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/drugs")
class DrugController(private val drugService: DrugService) {

    @GetMapping
    fun getDrugs(
        pageable: Pageable,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) nameContains: String?,
        @RequestParam(required = false) activeIngredient: String?,
        @RequestParam(required = false) criticalAmountInDays: Int?
    ): Page<Drug> {
        return drugService.search(
            pageable,
            name,
            nameContains,
            activeIngredient,
            criticalAmountInDays
        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Drug =
        drugService.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Drug with id $id not found"
            )
}