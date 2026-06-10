package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Nurse
import org.hospitalmanagement.models.enums.Gender
import org.hospitalmanagement.services.CryptoUtility
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component

@Component
class NurseSpecifications(private val cryptoUtility: CryptoUtility) {

    fun hasFirstName(firstName: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(firstName)
            cb.equal(person(root).get<String>("firstNameHash"), blindIndex)
        }

    fun hasLastName(lastName: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(lastName)
            cb.equal(person(root).get<String>("lastNameHash"), blindIndex)
        }

    fun hasEmail(email: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(email)
            cb.equal(person(root).get<String>("emailHash"), blindIndex)
        }

    fun hasCity(city: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(city)
            cb.equal(person(root).get<String>("cityHash"), blindIndex)
        }

    fun hasBirthday(birthday: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(birthday)
            cb.equal(person(root).get<String>("birthdayHash"), blindIndex)
        }

    fun hasGender(gender: Gender): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(person(root).get<Gender>("gender"), gender)
        }

    fun hasPlz(plz: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(plz)
            cb.equal(person(root).get<String>("plzHash"), blindIndex)
        }

    fun hasStreet(street: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(street)
            cb.equal(person(root).get<String>("streetHash"), blindIndex)
        }

    fun hasStreetNo(streetNo: Int): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(person(root).get<Int>("streetNo"), streetNo)
        }

    fun hasPhone(phone: String): Specification<Nurse> =
        Specification { root, _, cb ->
            val blindIndex = cryptoUtility.generateBlindIndex(phone)
            cb.equal(person(root).get<String>("phoneHash"), blindIndex)
        }

    // country is stored as plaintext (no encryption / no blind index)
    fun hasCountry(country: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(cb.lower(person(root).get("country")), country.lowercase())
        }

    fun hasDepartmentId(departmentId: Long): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("employee").get<Long>("department"),
                departmentId
            )
        }

    fun hasStationId(stationId: Int): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(root.get<Any>("station").get<Int>("id"), stationId)
        }

    fun afterId(id: Long): Specification<Nurse> =
        Specification { root, _, cb -> cb.greaterThan(root.get("id"), id) }

    private fun person(root: jakarta.persistence.criteria.Root<Nurse>) =
        root.get<Any>("employee").get<Any>("person")
}
