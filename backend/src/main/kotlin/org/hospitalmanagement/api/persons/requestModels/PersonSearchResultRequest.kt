package org.hospitalmanagement.api.persons.requestModels

data class PersonSearchResultRequest(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val birthday: String,
    val isEmployee: Boolean,
    val isPatient: Boolean
)
