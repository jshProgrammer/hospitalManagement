package org.hospitalmanagement.models.classes.facilities

data class Station(
    var id: Int,
    var name: String,
    var department: Department
)