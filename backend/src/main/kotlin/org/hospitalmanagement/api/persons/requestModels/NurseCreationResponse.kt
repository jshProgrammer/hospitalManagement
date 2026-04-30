package org.hospitalmanagement.api.persons.requestModels

data class NurseCreationResponse(
    val potentialMatches: List<PersonSearchResultRequest>? = null,
    val createdNurse: NurseRequest? = null
)