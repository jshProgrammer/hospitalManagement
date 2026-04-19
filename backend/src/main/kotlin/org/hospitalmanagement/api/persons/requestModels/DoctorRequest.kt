package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.models.enums.DoctorsType
import java.util.UUID

data class DoctorRequest(
    val id: UUID,
    val employeeId: UUID,
    val personId: UUID,
    val workPhone: String,
    val type: DoctorsType,
    val department: Int
)