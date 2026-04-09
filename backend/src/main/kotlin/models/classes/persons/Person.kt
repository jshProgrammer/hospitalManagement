package org.example.models.classes.persons

import Gender
import java.util.Date

data class Person(
    val id: String,
    val gender: Gender,
    val first_name: String,
    val last_name: String,
    val plz: Int,
    val city: String,
    val street: String,
    val street_no: Int,
    val country: String,
    val birthday: Date,
    val phone: String,
    val email: String
)
