package org.hospitalmanagement.db

import org.hospitalmanagement.models.classes.facilities.Station

data class Room(
    val id: Long,
    val station: Station,
    val number: Long,
    val floor: Long,
    val beds: Long
)