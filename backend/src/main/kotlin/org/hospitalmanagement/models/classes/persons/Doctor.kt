package org.hospitalmanagement.models.classes.persons
import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.models.enums.DoctorsTypeConverter

@Entity
@Table(
        name = "doctors",
        indexes = [
                Index(name = "doctors_pkey", columnList = "id", unique = true),
                Index(name = "idx_doctors_work_phone", columnList = "workPhone"),
                Index(name = "idx_doctors_type_id", columnList = "type, id")
        ]
)
class Doctor(
        @Id
        val id: Long? = null, // = employee id

        @OneToOne
        @MapsId
        @JoinColumn(name = "id")
        val employee: Employee,

        // + not included in workPhone
        val workPhone: String,

        @Convert(converter = DoctorsTypeConverter::class)
        @ColumnTransformer(read = "type::text", write = "?::doctors_type")
        val type: DoctorsType
)
