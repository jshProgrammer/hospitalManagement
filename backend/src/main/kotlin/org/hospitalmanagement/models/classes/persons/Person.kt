package org.hospitalmanagement.models.classes.persons

import java.util.Date
import jakarta.persistence.*
import org.hospitalmanagement.models.enums.Gender
import java.util.UUID

@Entity
@Table(name = "person")
class Person(
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