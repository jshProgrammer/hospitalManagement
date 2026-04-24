package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.medication.Diagnosis
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface DiagnosisRepository : JpaRepository<Diagnosis, Long>, JpaSpecificationExecutor<Diagnosis>