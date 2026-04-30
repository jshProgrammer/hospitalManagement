package org.hospitalmanagement.api.persons.requestModels

data class DoctorCreationResponse(
    val potentialMatches: List<PersonSearchResultRequest>? = null,
    val createdDoctor: DoctorRequest? = null
)