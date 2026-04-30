package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Employee
import org.hospitalmanagement.models.classes.persons.Person
import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.jpa.domain.Specification
import java.util.*

object DoctorSpecifications {
    fun hasFirstName(firstName: String): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("firstName")),
                firstName.lowercase()
            )
        }

    fun hasLastName(lastName: String): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("lastName")),
                lastName.lowercase()
            )
        }

    fun hasEmail(email: String): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("email")),
                email.lowercase()
            )
        }

    fun hasCity(city: String): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("city")),
                city.lowercase()
            )
        }

    fun hasBirthday(birthday: Date): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("birthday")),
                birthday
            )
        }

    fun hasGender(gender: Gender): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(person.get<Gender>("gender"), gender)
        }

    fun hasPlz(plz: Int): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                person.get<Int>("plz"),
                plz
            )
        }

    fun hasStreet(street: String): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("street")),
                street.lowercase()
            )
        }

    fun hasStreetNo(streetNo: Int): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                person.get<Int>("streetNo"),
                streetNo
            )
        }

    fun hasPhone(phone: String): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("phone")),
                phone.lowercase()
            )
        }

    fun hasCountry(country: String): Specification<Doctor> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("country")),
                country.lowercase()
            )
        }


    fun hasDepartmentId(departmentId: Long): Specification<Doctor> =
        Specification{root, _, cb ->
            cb.equal(
                root.get<Employee>("employee").get<Long>("department"),
                departmentId
            )
        }

    fun hasType(type: DoctorsType): Specification<Doctor> =
        Specification { root, _, cb ->
            cb.equal(
                root.get<DoctorsType>("type"),
                type
            )
        }

    fun hasWorkphone(workphone: String): Specification<Doctor> =
        Specification { root, _, cb ->

            cb.equal(
                cb.lower(root.get<String>("workPhone")),
                workphone.lowercase()
            )
        }
}
