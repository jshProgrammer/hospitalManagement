package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.models.enums.DoctorsType

data class EmployeeCreationRequest(
    val department: Long,
    val workPhone: String?,       // required for Doctor, ignored for Nurse
    val doctorType: DoctorsType? = null,  // required for Doctor
    val stationId: Long? = null  // required for Nurse
)
