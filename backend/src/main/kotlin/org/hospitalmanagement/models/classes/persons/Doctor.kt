package org.hospitalmanagement.models.classes.persons
import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.models.enums.DoctorsTypeConverter

@Entity
@Table(name = "doctors")
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
