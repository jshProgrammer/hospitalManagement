package org.hospitalmanagement.models.classes.persons

import org.hospitalmanagement.models.classes.facilities.Station


data class Nurse (
    val id: String,
    val station: Station,
)