package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hospitalmanagement.models.enums.DrugsType
import jakarta.persistence.*

@Entity
@Table(name = "drugs")
class Drug(
    @Id
    val id: Long,

    val stock: Long,

    val name: String,

    val activeIngredient: String,

    @Convert(converter = DrugsType.DrugsTypeConverter::class)
    val type: DrugsType
)