package org.hospitalmanagement.services.medication

import org.hospitalmanagement.dbRepositories.medication.MedicationRepository
import org.hospitalmanagement.models.classes.medication.Medication
import org.hospitalmanagement.models.enums.DoseUnit
import org.hospitalmanagement.models.enums.DrugsType
import org.hospitalmanagement.specifications.medication.MedicationSpecification
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.util.Date
import org.hospitalmanagement.dbRepositories.medication.DoseRepository
import org.hospitalmanagement.dbRepositories.medication.DrugsRepository
import org.hospitalmanagement.api.medication.requestModels.MedicationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Service
class MedicationService(
    private val medicationRepository: MedicationRepository,
    private val doseRepository: DoseRepository,
    private val drugsRepository: DrugsRepository
) {

    fun search(
        pageable: Pageable,
        drugId: Long?,
        drugType: DrugsType?,
        doseUnit: DoseUnit?,
        startedAfter: Date?,
        startedBefore: Date?,
        active: Boolean?
    ): Page<Medication> {
        val spec = MedicationSpecification.build(
            drugId, drugType, doseUnit, startedAfter, startedBefore, active
        )
        return medicationRepository.findAll(spec, pageable)
    }

    fun searchPaginated(
        after: Long?,
        limit: Int,
        drugId: Long?,
        drugType: DrugsType?,
        doseUnit: DoseUnit?,
        startedAfter: Date?,
        startedBefore: Date?,
        active: Boolean?
    ): List<Medication> {
        var spec = MedicationSpecification.build(
            drugId, drugType, doseUnit, startedAfter, startedBefore, active
        )
        if (after != null) spec = spec.and(MedicationSpecification.afterId(after))
        val pageable = PageRequest.of(0, limit, Sort.by(Sort.Order.asc("id")))
        return medicationRepository.findAll(spec, pageable).content
    }

    fun findById(id: Long): Medication? =
        medicationRepository.findById(id).orElse(null)

    fun create(request: MedicationRequest): Medication {
        val dose = doseRepository.findById(request.doseId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Dose with id ${request.doseId} not found") }
        val drug = drugsRepository.findById(request.drugId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Drug with id ${request.drugId} not found") }

        return medicationRepository.save(
            Medication(
                dose = dose,
                drug = drug,
                started = request.started,
                ended = request.ended
            )
        )
    }

    fun update(id: Long, request: MedicationRequest): Medication? {
        if (!medicationRepository.existsById(id)) return null

        val dose = doseRepository.findById(request.doseId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Dose with id ${request.doseId} not found") }
        val drug = drugsRepository.findById(request.drugId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Drug with id ${request.drugId} not found") }

        return medicationRepository.save(
            Medication(
                id = id,
                dose = dose,
                drug = drug,
                started = request.started,
                ended = request.ended
            )
        )
    }

}