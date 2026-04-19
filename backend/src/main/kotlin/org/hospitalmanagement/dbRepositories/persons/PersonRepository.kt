package org.hospitalmanagement.dbRepositories.persons

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.hospitalmanagement.models.classes.persons.Person
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

@Repository
interface PersonRepository: JpaRepository<Person, String> {
    override fun findAll(pageable: Pageable): Page<Person>
    fun findByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndBirthday(
        firstName: String, lastName: String, birthday: Date
    ): List<Person>
    fun findPersonByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName: String, lastName: String): Optional<Person>
    fun findById(uuid: UUID): Person
}