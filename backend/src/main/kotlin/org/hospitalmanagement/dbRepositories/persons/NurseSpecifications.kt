package org.hospitalmanagement.dbRepositories.persons

import org.hospitalmanagement.models.classes.facilities.Station
import org.hospitalmanagement.models.classes.persons.Doctor
import org.hospitalmanagement.models.classes.persons.Employee
import org.hospitalmanagement.models.classes.persons.Nurse
import org.hospitalmanagement.models.classes.persons.Person
import org.hospitalmanagement.models.enums.Gender
import org.springframework.data.jpa.domain.Specification
import java.util.*

object NurseSpecifications {
    fun hasFirstName(firstName: String): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("firstName")),
                firstName.lowercase()
            )
        }

    fun hasLastName(lastName: String): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("lastName")),
                lastName.lowercase()
            )
        }

    fun hasEmail(email: String): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("email")),
                email.lowercase()
            )
        }

    fun hasCity(city: String): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("city")),
                city.lowercase()
            )
        }

    fun hasBirthday(birthday: Date): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("birthday")),
                birthday
            )
        }

    fun hasGender(gender: Gender): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(person.get<Gender>("gender"), gender)
        }

    fun hasPlz(plz: Int): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                person.get<Int>("plz"),
                plz
            )
        }

    fun hasStreet(street: String): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("street")),
                street.lowercase()
            )
        }

    fun hasStreetNo(streetNo: Int): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                person.get<Int>("streetNo"),
                streetNo
            )
        }

    fun hasPhone(phone: String): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("phone")),
                phone.lowercase()
            )
        }

    fun hasCountry(country: String): Specification<Nurse> =
        Specification { root, _, cb ->

            val employee = root.get<Employee>("employee")
            val person = employee.get<Person>("person")

            cb.equal(
                cb.lower(person.get("country")),
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
        Specification { root, _, cb ->

            val station = root.get<Station>("station")

            cb.equal(
                station.get<Int>("id"),
                stationId
            )
        }
}