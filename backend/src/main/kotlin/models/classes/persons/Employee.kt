package org.hospitalmanagement.models.classes.persons

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "employee")
class Employee(
    @Id
    val id: String,

    @OneToOne(fetch = FetchType.LAZY) // Lazy schont die Performance
    @JoinColumn(name = "person_id") // Hier liegt der Fremdschlüssel in der DB
    val person: Person,

    // Falls Department noch nicht als Entity existiert, hier vorerst als String oder neue Entity
    val departmentId: String
)

/*
class Employee(
    val id: String,
    val person: Person,
    val department: Department
)*/
