package org.hospitalmanagement.services.medication

import org.hospitalmanagement.api.medication.requestModels.DoseRequest
import org.hospitalmanagement.dbRepositories.medication.DoseRepository
import org.hospitalmanagement.models.classes.medication.Dose
import org.hospitalmanagement.models.enums.DoseUnit
import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.specifications.medication.DoseSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

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

    fun create(request: DoseRequest): Dose {
        if (request.amount <= 0 || request.frequencyAmount <= 0) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Dose amount and frequency amount must be positive"
            )
        }

        return doseRepository.save(
            Dose(
                id = 0,
                unit = request.unit,
                amount = request.amount,
                frequency = request.frequency,
                frequencyAmount = request.frequencyAmount
            )
        )
    }

}
