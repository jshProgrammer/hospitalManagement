package org.hospitalmanagement.service.persons

import org.hospitalmanagement.api.persons.requestModels.PersonCreateRequest
import org.hospitalmanagement.api.persons.requestModels.PersonSearchResultRequest
import org.hospitalmanagement.dbRepositories.persons.EmployeeRepository
import org.hospitalmanagement.dbRepositories.persons.PatientRepository
import org.hospitalmanagement.dbRepositories.persons.PersonRepository
import org.hospitalmanagement.models.classes.persons.Person
import org.hospitalmanagement.services.CryptoUtility
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class PersonService(
    private val personRepository: PersonRepository,
    private val patientRepository: PatientRepository,
    private val employeeRepository: EmployeeRepository,
    private val cryptoUtility: CryptoUtility
) {
    fun findSimilarPersons(personData: PersonCreateRequest): List<PersonSearchResultRequest> {
        val firstNameHash = cryptoUtility.generateBlindIndex(personData.firstName)
        val lastNameHash = cryptoUtility.generateBlindIndex(personData.lastName)
        val birthdayHash = cryptoUtility.generateBlindIndex(personData.birthday)

        val matches = personRepository.findByFirstNameHashAndLastNameHashAndBirthdayHash(
            firstNameHash,
            lastNameHash,
            birthdayHash
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
            firstNameHash = cryptoUtility.generateBlindIndex(personData.firstName),
            lastNameHash = cryptoUtility.generateBlindIndex(personData.lastName),
            plz = personData.plz,
            plzHash = cryptoUtility.generateBlindIndex(personData.plz),
            city = personData.city,
            cityHash = cryptoUtility.generateBlindIndex(personData.city),
            street = personData.street,
            streetHash = cryptoUtility.generateBlindIndex(personData.street),
            streetNo = personData.houseNumber.toIntOrNull() ?: 0,
            country = personData.country,
            birthday = personData.birthday,
            birthdayHash = cryptoUtility.generateBlindIndex(personData.birthday),
            phone = personData.phoneNumber,
            phoneHash = cryptoUtility.generateBlindIndex(personData.phoneNumber),
            email = personData.email,
            emailHash = cryptoUtility.generateBlindIndex(personData.email)
        )
        return personRepository.save(person)
    }

    fun findById(personId: Long): Person =
        personRepository.findById(personId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Person with id $personId not found") }
}

