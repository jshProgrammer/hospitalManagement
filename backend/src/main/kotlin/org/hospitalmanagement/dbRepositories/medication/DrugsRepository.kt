package org.hospitalmanagement.dbRepositories.medication

import org.hospitalmanagement.models.classes.facilities.Department
import org.hospitalmanagement.models.classes.medication.Drug
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

@Repository
interface DrugsRepository: JpaRepository<Drug, Long>, JpaSpecificationExecutor<Drug>{
}