package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.models.enums.DoseUnit
import jakarta.persistence.*


@Entity
@Table(name = "dose")
class Dose(
    @Id
    val id: Long,

    @Convert(converter = DoseUnit.DoseUnitConverter::class)
    val unit: DoseUnit,

    val amount: Int,

    @Convert(converter = DoseFrequency.DoseFrequencyConverter::class)
    val frequency: DoseFrequency,

    val frequencyAmount: Int
)