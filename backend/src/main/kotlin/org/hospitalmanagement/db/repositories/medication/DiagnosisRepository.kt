package org.hospitalmanagement.db.repositories.medication
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.classes.medication.Medication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DiagnosisRepository: JpaRepository<Diagnosis, String> {
    override fun findAll(pageable: Pageable): Page<Diagnosis>
    fun findById(id: Long): Optional<Diagnosis>
    fun findByDisease(name: String): List<Diagnosis>
    fun findByMedication(medication: Medication): List<Diagnosis>
    fun findByDiagnosedPatient_Id(diagnosed_patient: UUID): List<Diagnosis> //uses the PERSON id, NOT the patient id
}