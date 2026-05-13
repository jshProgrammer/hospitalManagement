package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.models.enums.DoctorsType

data class DoctorRequest(
    val id: Long,
    val employeeId: Long,
    val personId: Long,
    val workPhone: String,
    val type: DoctorsType,
    val department: Long
)
