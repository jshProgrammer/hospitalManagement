package org.hospitalmanagement.models.classes.medication

import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.models.enums.DoseUnit


data class Dose(
    val id: Long,
    val unit: DoseUnit,
    val amount: Int,
    val frequency: DoseFrequency,
    val frequencyAmount: Int
)