package org.hospitalmanagement.api.persons.requestModels

import jakarta.validation.constraints.Email
import org.hospitalmanagement.config.validation.ValidBirthday
import org.hospitalmanagement.config.validation.ValidGermanPhone
import org.hospitalmanagement.config.validation.ValidName
import org.hospitalmanagement.config.validation.ValidPlz
import org.hospitalmanagement.models.enums.Gender

data class PersonCreateRequest(
    val gender: Gender,
    @ValidName
    val firstName: String,
    @ValidName
    val lastName: String,
    @Email(message = "E-Mail-Adresse ist ungültig")
    val email: String,
    @ValidGermanPhone
    val phoneNumber: String,
    @ValidPlz
    val plz: String,
    @ValidName
    val city: String,
    @ValidName
    val street: String,
    @ValidName
    val houseNumber: String,
    @ValidName
    val country: String,
    @ValidBirthday
    val birthday: String
)