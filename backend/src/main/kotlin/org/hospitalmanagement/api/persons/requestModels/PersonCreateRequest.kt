package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.config.validation.ValidBirthday
import org.hospitalmanagement.config.validation.ValidPlz
import org.hospitalmanagement.models.enums.Gender

data class PersonCreateRequest(
    val gender: Gender,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    @ValidPlz
    val plz: String,
    val city: String,
    val street: String,
    val houseNumber: String,
    val country: String,
    @ValidBirthday
    val birthday: String
)