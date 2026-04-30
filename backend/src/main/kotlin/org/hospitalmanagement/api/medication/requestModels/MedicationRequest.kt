package org.hospitalmanagement.api.medication.requestModels

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class MedicationRequest(
    @JsonProperty("dose_id")
    val doseId: Long,

    @JsonProperty("drug_id")
    val drugId: Long,

    val started: Date?,
    val ended: Date?
)