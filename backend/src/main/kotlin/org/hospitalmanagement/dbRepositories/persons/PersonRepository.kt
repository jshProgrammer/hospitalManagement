package org.hospitalmanagement.dbRepositories.persons

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.hospitalmanagement.models.classes.persons.Person
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

@Repository
interface PersonRepository: JpaRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    override fun findAll(pageable: Pageable): Page<Person>

    // Legacy methods (deprecated - use hash-based methods instead)
    fun findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthday(
        firstName: String, lastName: String, birthday: Date
    ): List<Person>
    fun findPersonByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName: String, lastName: String): Optional<Person>

    // Hash-based search methods for encrypted fields
    fun findByFirstNameHashAndLastNameHashAndBirthdayHash(
        firstNameHash: String, lastNameHash: String, birthdayHash: String
    ): List<Person>

    fun findByFirstNameHashAndLastNameHash(
        firstNameHash: String, lastNameHash: String
    ): Optional<Person>

    fun findByEmailHash(emailHash: String): Optional<Person>

    fun existsByEmailHash(emailHash: String): Boolean
}

