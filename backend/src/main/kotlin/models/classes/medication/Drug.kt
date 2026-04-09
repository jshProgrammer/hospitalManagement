package org.example.models.classes.medication

import DrugsType

data class Drug(
    val id: Long,
    val stock: Long,
    val name: String,
    val activeIngredient: String,
    val type: DrugsType
)