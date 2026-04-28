package org.hospitalmanagement.api.medication.requestModels

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.Date
import java.util.UUID

data class DiagnosisRequest(
    val disease: String,

    @JsonProperty("medication_id")
    val medicationId: Long,

    @JsonProperty("diagnosed_by")
    val diagnosedBy: UUID,

    @JsonProperty("diagnosed_patient")
    val diagnosedPatient: Long,

    @JsonProperty("diagnosed_at")
    val diagnosedAt: Date
)