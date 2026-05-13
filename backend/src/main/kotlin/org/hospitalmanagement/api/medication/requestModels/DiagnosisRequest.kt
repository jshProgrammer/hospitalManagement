package org.hospitalmanagement.api.medication.requestModels

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date

data class DiagnosisRequest(
    val disease: String,

    @JsonProperty("medication_id")
    val medicationId: Long,

    @JsonProperty("diagnosed_by")
    val diagnosedBy: Long,

    @JsonProperty("diagnosed_patient")
    val diagnosedPatient: Long,

    @JsonProperty("diagnosed_at")
    val diagnosedAt: Date
)
