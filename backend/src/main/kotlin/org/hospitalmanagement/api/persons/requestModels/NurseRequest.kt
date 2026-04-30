package org.hospitalmanagement.api.persons.requestModels

import java.util.UUID

data class NurseRequest(
    val id: UUID,
    val stationId: Long
)