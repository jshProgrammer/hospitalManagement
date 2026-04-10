package org.hospitalmanagement.models.classes.persons
import org.hospitalmanagement.models.enums.DoctorsType

data class Doctor(
        val id: String,
        val employee: Employee,
        val work_phone: String,
        val type: DoctorsType
)