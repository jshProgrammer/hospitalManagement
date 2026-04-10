package org.hospitalmanagement.models.classes.medication

import org.hospitalmanagement.models.enums.DrugsType


data class Drug(
    val id: Long,
    val stock: Long,
    val name: String,
    val activeIngredient: String,
    val type: DrugsType
)