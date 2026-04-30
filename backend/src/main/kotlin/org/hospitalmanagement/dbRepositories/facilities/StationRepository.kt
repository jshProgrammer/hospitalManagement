package org.hospitalmanagement.dbRepositories.facilities

import org.hospitalmanagement.models.classes.facilities.Station
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StationRepository: JpaRepository<Station, String>, JpaSpecificationExecutor<Station> {
    fun findById(id: Long): Optional<Station>
}