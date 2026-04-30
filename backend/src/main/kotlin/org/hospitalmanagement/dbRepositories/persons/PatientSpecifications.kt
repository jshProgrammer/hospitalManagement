import org.hospitalmanagement.models.classes.persons.Patient
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.jpa.domain.Specification
import java.util.*

object PatientSpecifications {

    fun hasFirstName(firstName: String): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("firstName")),
                firstName.lowercase()
            )
        }

    fun hasLastName(lastName: String): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("lastName")),
                lastName.lowercase()
            )
        }

    fun hasEmail(email: String): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("email")),
                email.lowercase()
            )
        }

    fun hasCity(city: String): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("city")),
                city.lowercase()
            )
        }

    fun hasBirthday(birthday: Date): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Date>("birthday"),
                birthday
            )
        }

    // Felder die direkt in Patient liegen:
    fun hasGender(gender: Gender): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(root.get<Gender>("gender"), gender)
        }

    fun hasPlz(plz: Int): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<Any>("person").get<Int>("plz"),
                plz
            )
        }

    fun hasStreet(street: String): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("street")),
                street.lowercase()
            )
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
            cb.equal(
                root.get<Any>("person").get<String>("phone"),
                phone
            )
        }

    fun hasCountry(country: String): Specification<Patient> =
        Specification { root, _, cb ->
            cb.equal(
                cb.lower(root.get<Any>("person").get("country")),
                country.lowercase()
            )
        }
}