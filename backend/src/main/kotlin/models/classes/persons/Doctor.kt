package org.example.models.classes.persons
import DoctorsType

data class Doctor(
        val id: String,
        val employee: Employee,
        val work_phone: String,
        val type: DoctorsType
)