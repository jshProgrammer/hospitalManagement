package org.hospitalmanagement.models.classes.persons

import java.util.Date
import jakarta.persistence.*
import org.hospitalmanagement.models.enums.Gender
import org.hospitalmanagement.config.converters.EncryptedStringConverter

@Entity
@Table(
    name = "person",
    indexes = [
        Index(name = "person_pkey", columnList = "id", unique = true),
        Index(name = "idx_person_firstName_hash", columnList = "firstName_hash"),
        Index(name = "idx_person_lastName_hash", columnList = "lastName_hash"),
        Index(name = "idx_person_email_hash", columnList = "email_hash"),
        Index(name = "idx_person_city_hash", columnList = "city_hash"),
        Index(name = "idx_person_lastName_firstName_hash", columnList = "lastName_hash, firstName_hash")
    ]
)
class Person(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @Column(name = "firstName_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    var firstName: String,

    @Column(name = "lastName_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    var lastName: String,

    @Column(name = "firstName_hash")
    var firstNameHash: String = "",

    @Column(name = "lastName_hash")
    var lastNameHash: String = "",

    @Column(name = "plz_encrypted")
    var plz: Int,

    @Column(name = "city_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    var city: String,

    @Column(name = "city_hash")
    var cityHash: String = "",

    @Column(name = "street_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    var street: String,

    @Column(name = "street_hash")
    var streetHash: String? = null,

    var streetNo: Int,

    var country: String,

    @Temporal(TemporalType.DATE)
    @Column(name = "birthday_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    var birthday: Date,

    @Column(name = "birthday_hash")
    var birthdayHash: String? = null,

    @Column(name = "phone_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    var phone: String,

    @Column(name = "phone_hash")
    var phoneHash: String? = null,

    @Column(name = "email_encrypted")
    @Convert(converter = EncryptedStringConverter::class)
    var email: String,

    @Column(name = "email_hash", unique = true)
    var emailHash: String = ""
)

