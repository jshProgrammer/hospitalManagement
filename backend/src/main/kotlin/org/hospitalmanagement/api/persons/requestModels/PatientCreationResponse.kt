package org.hospitalmanagement.api.persons.requestModels

data class PatientCreationResponse(
    val potentialMatches: List<PersonSearchResultRequest>? = null,
    val createdPatient: PatientRequest? = null
)