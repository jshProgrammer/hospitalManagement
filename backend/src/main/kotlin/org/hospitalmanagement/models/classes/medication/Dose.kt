package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import org.hibernate.annotations.ColumnTransformer
import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.models.enums.DoseUnit
import jakarta.persistence.*


@Entity
@Table(
    name = "dose",
    indexes = [
        Index(name = "dose_pkey", columnList = "id", unique = true),
        Index(name = "idx_dose_unit_frequency", columnList = "unit, frequency")
    ]
)
class Dose(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Convert(converter = DoseUnit.DoseUnitConverter::class)
    @ColumnTransformer(read = "unit::text", write = "?::dose_unit")
    val unit: DoseUnit,

    val amount: Long,

    @Convert(converter = DoseFrequency.DoseFrequencyConverter::class)
    @ColumnTransformer(read = "frequency::text", write = "?::dose_frequency")
    val frequency: DoseFrequency,

    val frequencyAmount: Long
)
