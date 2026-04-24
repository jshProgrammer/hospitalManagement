package org.hospitalmanagement.specifications.medication

import org.hospitalmanagement.models.classes.medication.Dose
import org.hospitalmanagement.models.enums.DoseUnit
import org.hospitalmanagement.models.enums.DoseFrequency
import org.springframework.data.jpa.domain.Specification

object DoseSpecification {

    fun hasUnit(unit: DoseUnit?): Specification<Dose> =
        Specification { root, _, cb ->
            unit?.let { cb.equal(root.get<DoseUnit>("unit"), it) }
        }

    fun hasFrequency(frequency: DoseFrequency?): Specification<Dose> =
        Specification { root, _, cb ->
            frequency?.let { cb.equal(root.get<DoseFrequency>("frequency"), it) }
        }

    fun hasAmount(amount: Long?): Specification<Dose> =
        Specification { root, _, cb ->
            amount?.let { cb.equal(root.get<Long>("amount"), it) }
        }

    fun hasFrequencyAmount(frequencyAmount: Long?): Specification<Dose> =
        Specification { root, _, cb ->
            frequencyAmount?.let { cb.equal(root.get<Long>("frequencyAmount"), it) }
        }


   fun build(
       unit: DoseUnit?,
       frequency: DoseFrequency?,
       amount: Long?,
       frequencyAmount: Long?
    ): Specification<Dose> =
        Specification.where(hasUnit(unit))
            .and(hasFrequency(frequency))
            .and(hasAmount(amount))
            .and(hasFrequencyAmount(frequencyAmount))
}