package org.hospitalmanagement.services.medication

import org.hospitalmanagement.dbRepositories.medication.DoseRepository
import org.hospitalmanagement.models.classes.medication.Dose
import org.hospitalmanagement.models.enums.DoseUnit
import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.specifications.medication.DoseSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class DoseService(private val doseRepository: DoseRepository) {

    fun search(
        pageable: Pageable,
        unit: DoseUnit?,
        frequency: DoseFrequency?,
        amount: Long?,
        frequencyAmount: Long?
    ): Page<Dose> {
        val spec = DoseSpecification.build(unit, frequency, amount, frequencyAmount)
        return doseRepository.findAll(spec, pageable)
    }

    fun findById(id: Long): Dose? =
        doseRepository.findById(id).orElse(null)

}