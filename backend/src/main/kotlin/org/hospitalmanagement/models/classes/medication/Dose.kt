package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import org.hibernate.annotations.ColumnTransformer
import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.models.enums.DoseUnit
import jakarta.persistence.*


@Entity
@Table(name = "dose")
class Dose(
    @Id
    val id: Long,

    @Convert(converter = DoseUnit.DoseUnitConverter::class)
    @ColumnTransformer(read = "unit::text", write = "?::dose_unit")
    val unit: DoseUnit,

    val amount: Long,

    @Convert(converter = DoseFrequency.DoseFrequencyConverter::class)
    @ColumnTransformer(read = "frequency::text", write = "?::dose_frequency")
    val frequency: DoseFrequency,

    val frequencyAmount: Long
)
