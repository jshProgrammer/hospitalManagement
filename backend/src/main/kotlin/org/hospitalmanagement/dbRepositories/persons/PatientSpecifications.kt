package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.Gender
import org.hospitalmanagement.services.CryptoUtility
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component
import java.util.*

@Component
class PatientSpecifications(private val cryptoUtility: CryptoUtility) {

    fun hasFirstName(firstName: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(firstName)
            cb.equal(root.get<Any>("person").get<String>("firstNameHash"), blindIndex)
        }

    fun hasLastName(lastName: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(lastName)
            cb.equal(root.get<Any>("person").get<String>("lastNameHash"), blindIndex)
        }

    fun hasEmail(email: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(email)
            cb.equal(root.get<Any>("person").get<String>("emailHash"), blindIndex)
        }

    fun hasCity(city: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(city)
            cb.equal(root.get<Any>("person").get<String>("cityHash"), blindIndex)
        }

    fun hasBirthday(birthday: Date): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Date>("birthday"),
                birthday
            )
        }

    fun hasGender(gender: Gender): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Gender>("gender"),
                gender
            )
        }

    fun hasPlz(plz: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(plz)
            cb.equal(root.get<Any>("person").get<String>("plzHash"), blindIndex)
        }

    fun hasStreet(street: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(street)
            cb.equal(root.get<Any>("person").get<String>("streetHash"), blindIndex)
        }

    fun hasStreetNo(streetNo: Int): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Int>("streetNo"),
                streetNo
            )
        }

    fun hasPhone(phone: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(phone)
            cb.equal(root.get<Any>("person").get<String>("phoneHash"), blindIndex)
        }

    fun hasCountry(country: String): Specification<Patient> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(country)
            cb.equal(root.get<Any>("person").get<String>("countryHash"), blindIndex)
        }

    fun afterId(id: Long): Specification<Patient> =
        Specification { root, _, cb -> cb.greaterThan(root.get("id"), id) }
}