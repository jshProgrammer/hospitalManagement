package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.Date
import jakarta.persistence.*

@Entity
@Table(name = "medication")
class Medication(
    @Id
    val id: Long,

    @OneToOne
    @JoinColumn(name = "dosis")
    val dose: Dose,

    @OneToOne
    @JoinColumn(name = "drug")
    val drug: Drug,

    val started: Date?,

    val ended: Date?
)