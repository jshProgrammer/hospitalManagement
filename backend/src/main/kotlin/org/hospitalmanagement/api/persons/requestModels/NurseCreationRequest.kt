package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.models.enums.Gender
import java.util.*

data class NurseCreationRequest(
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
    val birthday: String,

    val department: Long,
    val stationId: Long
)
