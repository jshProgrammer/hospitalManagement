package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.ColumnTransformer
import org.hospitalmanagement.models.enums.DrugsType
import jakarta.persistence.*

@Entity
@Table(
    name = "drugs",
    indexes = [
        Index(name = "drugs_pkey", columnList = "id", unique = true),
        Index(name = "idx_drugs_name", columnList = "name"),
        Index(name = "idx_drugs_type_id", columnList = "type, id")
    ]
)
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
