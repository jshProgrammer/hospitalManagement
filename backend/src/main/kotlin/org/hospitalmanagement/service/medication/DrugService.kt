package org.hospitalmanagement.services.facilities

import org.hospitalmanagement.dbRepositories.medication.DrugsRepository
import org.hospitalmanagement.dbRepositories.medication.DrugsSpecifications
import org.hospitalmanagement.models.classes.medication.Drug
import org.hospitalmanagement.models.enums.DrugsType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class DrugService(private val drugsRepository: DrugsRepository) {
    fun findById(id: Long): Drug? =
        drugsRepository.findById(id).orElse(null)


    fun search(
        pageable: Pageable,
        name: String?,
        nameContains: String?,
        activeIngredient: String?,
        type: String?,
        criticalAmountInDays: Int?
    ): Page<Drug> {
        //TODO: add criticalAmountInDays parameter => takes stock and current bookings into consideration

        if (name != null && nameContains != null) {
            throw IllegalArgumentException("Use either name or nameContains, not both")
        }

        var spec: Specification<Drug>? = null

        if (name != null) {
            spec = Specification.where(DrugsSpecifications.hasName(name))
        }

        if (nameContains != null) {
            spec = (spec ?: Specification.where(null))
                .and(DrugsSpecifications.nameContains(nameContains))
        }

        if (activeIngredient != null) {
            spec = (spec ?: Specification.where(null))
                .and(DrugsSpecifications.hasActiveIngredient(activeIngredient))
        }

        val drugType = type?.let { t ->
            DrugsType.entries.find { it.name == t.uppercase() }
        }

        if (drugType != null) {
            spec = (spec ?: Specification.where(null))
                .and(DrugsSpecifications.hasType(drugType))
        }

        return if (spec != null) {
            drugsRepository.findAll(spec, pageable)
        } else {
            drugsRepository.findAll(pageable)
        }
    }

}