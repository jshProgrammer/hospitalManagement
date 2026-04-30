package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnTransformer
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
    @ColumnTransformer(read = "type::text", write = "?::drugs_type")
    val type: DrugsType
)
