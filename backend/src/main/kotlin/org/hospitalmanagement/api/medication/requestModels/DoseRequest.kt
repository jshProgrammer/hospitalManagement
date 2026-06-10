package org.hospitalmanagement.api.medication.requestModels

import org.hospitalmanagement.models.enums.DoseFrequency
import org.hospitalmanagement.models.enums.DoseUnit

data class DoseRequest(
    val unit: DoseUnit,
    val amount: Long,
    val frequency: DoseFrequency,
    val frequencyAmount: Long
)
