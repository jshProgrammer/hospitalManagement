package org.example.models.classes.persons

import org.example.models.classes.facilities.Department

data class Employee(
    val id: String,
    val person: Person,
    val department: Department
)
