package org.example.models.classes.facilities

data class Room(
    val id: Long,
    val station: Station,
    val number: Long,
    val floor: Long,
    val beds: Long
)