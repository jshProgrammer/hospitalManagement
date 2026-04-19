package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.models.enums.Gender
import java.util.Date

data class PersonCreateRequest(
    val gender: Gender,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val plz: Int,
    val city: String,
    val street: String,
    val houseNumber: String,
    val country: String,
    val birthday: Date
)