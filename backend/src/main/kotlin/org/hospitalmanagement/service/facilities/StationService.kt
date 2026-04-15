package org.hospitalmanagement.services.facilities

import org.hospitalmanagement.models.classes.facilities.Station
import org.hospitalmanagement.db.repositories.facilities.StationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class StationService(private val stationRepository: StationRepository) {

    fun findAll(pageable: Pageable): Page<Station> =
        stationRepository.findAll(pageable)

    fun findById(id: Long): Station? =
        stationRepository.findById(id).orElse(null)

    fun getByName(name: String): Station? =
        stationRepository.findByName(name).orElse(null)

}