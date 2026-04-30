package org.hospitalmanagement.api.persons.requestModels

import java.util.UUID

data class PatientRequest(
    val id: Long,
    val personId: UUID
)