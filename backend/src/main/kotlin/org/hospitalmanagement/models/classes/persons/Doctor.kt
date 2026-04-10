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

        // TODO: probably rename to workPhone
        // TODO: automatically add '+' to output as not included in db
        val work_phone: String,

        @Convert(converter = DoctorsTypeConverter::class)
        val type: DoctorsType
)