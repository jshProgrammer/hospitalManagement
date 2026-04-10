package org.hospitalmanagement.models.classes.persons

import java.util.Date
import jakarta.persistence.*
import org.hospitalmanagement.models.enums.Gender
import java.util.UUID

@Entity
@Table(name = "person")
class Person(
    //TODO: try Int instead of UUID
    @Id
    @GeneratedValue
    var id: UUID? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender,

    var firstName: String,
    var lastName: String,
    var plz: Int,
    var city: String,
    var street: String,
    var streetNo: Int,
    var country: String,

    @Temporal(TemporalType.DATE)
    var birthday: Date,

    var phone: String,
    var email: String
)

/*
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
)*/
