package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.api.persons.requestModels.PersonSearchResultRequest
import org.hospitalmanagement.dbRepositories.persons.EmployeeRepository
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.dbRepositories.persons.PersonRepository
import org.hospitalmanagement.models.classes.persons.Person
import org.springframework.stereotype.Service
import java.util.*

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val patientRepository: PatientRepository,
    private val employeeRepository: EmployeeRepository
) {
    fun findSimilarPersons(personData: PersonCreateRequest): List<PersonSearchResultRequest> {
        //TODO: We can also add more search criteria here, like city or stuff like that
        val matches = personRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthday(
            personData.firstName,
            personData.lastName,
            personData.birthday
        )

        return matches.map { person ->
            PersonSearchResultRequest(
                id = person.id!!,
                firstName = person.firstName,
                lastName = person.lastName,
                birthday = person.birthday,
                isEmployee = employeeRepository.existsByPersonId(person.id!!),
                isPatient = patientRepository.existsByPersonId(person.id!!)
            )
        }
    }

    fun createPerson(personData: PersonCreateRequest): Person {
        val person = Person(
            gender = personData.gender,
            firstName = personData.firstName,
            lastName = personData.lastName,
            plz = personData.plz,
            city = personData.city,
            street = personData.street,
            streetNo = personData.houseNumber.toIntOrNull() ?: 0,
            country = personData.country,
            birthday = personData.birthday,
            phone = personData.phoneNumber,
            email = personData.email
        )
        return personRepository.save(person)
    }

    fun findById(personId: UUID): Person =
        personRepository.findById(personId)

}