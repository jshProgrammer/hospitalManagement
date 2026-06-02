package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.models.enums.Gender

data class PersonCreateRequest(
    val gender: Gender,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val plz: String,
    val city: String,
    val street: String,
    val houseNumber: String,
    val country: String,
    val birthday: String
)