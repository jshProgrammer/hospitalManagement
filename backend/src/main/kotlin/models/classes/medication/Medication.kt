package org.example.models.classes.medication

import java.util.Date

data class Medication(
    val id: Long,
    val dosis: Dose,
    val drug: Drug,
    val started: Date?,
    val ended: Date?
)