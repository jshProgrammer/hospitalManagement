package org.hospitalmanagement.db.repositories
import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.hospitalmanagement.models.classes.medication.Medication
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.Optional
@Repository
interface DiagnosisRepository: JpaRepository<Diagnosis, String> {
    override fun findAll(pageable: Pageable): Page<Diagnosis>
    fun findById(id: Long): Optional<Diagnosis>
    fun findByDisease(name: String): List<Diagnosis>
    //fun findByMedication(medication: Medication): List<Diagnosis>
    //fun findByPatientId(patientId: UUID): Optional<Diagnosis> //uses the PERSON id, NOT the patient id
    //fun findByDiagnosedById(diagnosedBy: LocalDate): List<Diagnosis>

}