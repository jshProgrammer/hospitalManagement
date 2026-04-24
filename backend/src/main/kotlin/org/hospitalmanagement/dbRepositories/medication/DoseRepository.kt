package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.medication.Dose
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface DoseRepository : JpaRepository<Dose, Long>, JpaSpecificationExecutor<Dose>