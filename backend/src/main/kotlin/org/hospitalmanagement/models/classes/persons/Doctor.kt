package org.hospitalmanagement.models.classes.persons
import jakarta.persistence.*
import org.hospitalmanagement.models.enums.DoctorsType
import org.hospitalmanagement.models.enums.DoctorsTypeConverter
import java.util.UUID

@Entity
@Table(name = "doctors")
class Doctor(
        @Id
        val id: UUID? = null, // = employee id

        @OneToOne
        @MapsId
        @JoinColumn(name = "id")
        val employee: Employee,

        // + not included in workPhone
        val workPhone: String,

        @Convert(converter = DoctorsTypeConverter::class)
        val type: DoctorsType
)