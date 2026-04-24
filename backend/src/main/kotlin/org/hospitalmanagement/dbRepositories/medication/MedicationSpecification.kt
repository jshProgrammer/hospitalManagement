package org.hospitalmanagement.specifications.medication

import org.hospitalmanagement.models.classes.medication.Medication
import org.hospitalmanagement.models.enums.DoseUnit
import org.hospitalmanagement.models.enums.DrugsType
import org.springframework.data.jpa.domain.Specification
import java.util.Date

object MedicationSpecification {

    fun hasDrugId(drugId: Long?): Specification<Medication> =
        Specification { root, _, cb ->
            drugId?.let { cb.equal(root.get<Any>("drug").get<Long>("id"), it) }
        }

    fun hasDrugType(type: DrugsType?): Specification<Medication> =
        Specification { root, _, cb ->
            type?.let { cb.equal(root.get<Any>("drug").get<DrugsType>("type"), it) }
        }

    fun hasDoseUnit(unit: DoseUnit?): Specification<Medication> =
        Specification { root, _, cb ->
            unit?.let { cb.equal(root.get<Any>("dose").get<DoseUnit>("unit"), it) }
        }

    fun startedAfter(date: Date?): Specification<Medication> =
        Specification { root, _, cb ->
            date?.let { cb.greaterThanOrEqualTo(root.get("started"), it) }
        }

    fun startedBefore(date: Date?): Specification<Medication> =
        Specification { root, _, cb ->
            date?.let { cb.lessThanOrEqualTo(root.get("started"), it) }
        }

    fun isActive(active: Boolean?): Specification<Medication> =
        Specification { root, _, cb ->
            when (active) {
                true -> cb.isNull(root.get<Date>("ended"))
                false -> cb.isNotNull(root.get<Date>("ended"))
                null -> null
            }
        }

    fun build(
        drugId: Long?,
        drugType: DrugsType?,
        doseUnit: DoseUnit?,
        startedAfter: Date?,
        startedBefore: Date?,
        active: Boolean?
    ): Specification<Medication> =
        Specification.where(hasDrugId(drugId))
            .and(hasDrugType(drugType))
            .and(hasDoseUnit(doseUnit))
            .and(startedAfter(startedAfter))
            .and(startedBefore(startedBefore))
            .and(isActive(active))

}