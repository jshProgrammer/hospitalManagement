package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.medication.Medication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface MedicationRepository : JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication>