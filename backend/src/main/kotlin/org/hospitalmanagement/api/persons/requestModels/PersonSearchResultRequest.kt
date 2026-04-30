package org.hospitalmanagement.api.persons.requestModels

import java.util.Date
import java.util.UUID

data class PersonSearchResultRequest(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val birthday: Date,
    val isEmployee: Boolean,
    val isPatient: Boolean
)