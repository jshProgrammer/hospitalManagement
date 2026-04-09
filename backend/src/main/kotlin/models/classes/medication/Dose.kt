package org.example.models.classes.medication

import DoseUnit
import org.example.models.enums.DoseFrequency

data class Dose(
    val id: Long,
    val unit: DoseUnit,
    val amount: Int,
    val frequency: DoseFrequency,
    val frequencyAmount: Int
)