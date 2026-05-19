package org.hospitalmanagement.models.classes.medication

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.util.Date
import jakarta.persistence.*

@Entity
@Table(
    name = "medication",
    indexes = [
        Index(name = "medication_pkey", columnList = "id", unique = true),
        Index(name = "idx_medication_dosis", columnList = "dosis"),
        Index(name = "idx_medication_drug_started", columnList = "drug, started"),
        Index(name = "idx_medication_started_id", columnList = "started, id")
    ]
)
class Medication(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @OneToOne
    @JoinColumn(name = "dosis")
    val dose: Dose,

    @OneToOne
    @JoinColumn(name = "drug")
    val drug: Drug,

    @Temporal(TemporalType.DATE)
    val started: Date?,

    @Temporal(TemporalType.DATE)
    val ended: Date?
)
