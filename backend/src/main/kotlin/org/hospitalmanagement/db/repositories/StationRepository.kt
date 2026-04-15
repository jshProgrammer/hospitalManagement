package org.hospitalmanagement.db.repositories

import org.hospitalmanagement.models.classes.facilities.Station
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StationRepository: JpaRepository<Station, String> {
    override fun findAll(pageable: Pageable): Page<Station>
    fun findById(id: Long): Station

}