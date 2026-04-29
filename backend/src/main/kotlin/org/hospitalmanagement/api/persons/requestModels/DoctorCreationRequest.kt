package org.hospitalmanagement.api.persons.requestModels

import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.models.enums.Gender
import java.time.LocalDate
import java.util.*

data class DoctorCreationRequest (
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
    val birthday: Date,

    val department: Long,
    val workPhone: String,
    val doctorType: DoctorsType
    )
