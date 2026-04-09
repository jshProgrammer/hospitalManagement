package org.example.models.classes.persons

import org.example.models.classes.facilities.Station

data class Nurse (
    val id: String,
    val station: Station
)