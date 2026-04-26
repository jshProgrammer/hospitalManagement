package org.hospitalmanagement.dbRepositories.persons

import jakarta.persistence.Id
import org.hospitalmanagement.models.classes.persons.Nurse
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.jpa.domain.Specification
import java.util.*

object NurseSpecifications {
    fun hasFirstName(firstName: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("firstName")),
                firstName.lowercase()
            )
        }

    fun hasLastName(lastName: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("lastName")),
                lastName.lowercase()
            )
        }

    fun hasEmail(email: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("email")),
                email.lowercase()
            )
        }

    fun hasCity(city: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("city")),
                city.lowercase()
            )
        }

    fun hasBirthday(birthday: Date): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Date>("birthday"),
                birthday
            )
        }

    // Felder die direkt in  liegen:
    fun hasGender(gender: Gender): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(root.get<Gender>("gender"), gender)
        }

    fun hasPlz(plz: Int): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Int>("plz"),
                plz
            )
        }

    fun hasStreet(street: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("street")),
                street.lowercase()
            )
        }

    fun hasStreetNo(streetNo: Int): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Int>("streetNo"),
                streetNo
            )
        }

    fun hasPhone(phone: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<String>("phone"),
                phone
            )
        }

    fun hasCountry(country: String): Specification<Nurse> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("country")),
                country.lowercase()
            )
        }

    fun hasDepartmentId(departmentId: Int): Specification<Nurse> =
        Specification{root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("employee").get("departmentId")),
                departmentId
            )
        }

    fun hasStationId(stationId: Int): Specification<Nurse> =
        Specification{root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("nurse").get("stationId")),
                stationId
            )
        }
}