package org.hospitalmanagement.services

import org.hospitalmanagement.db.Room
import org.hospitalmanagement.dbRepositories.facilities.RoomsRepository
import org.hospitalmanagement.dbRepositories.facilities.RoomsSpecifications
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class RoomsService(private val roomsRepository: RoomsRepository) {
    @Cacheable("rooms-search")
    fun search(
        pageable: Pageable,
        stationID: Long?,
        number: Long?
    ): Page<Room> {
        var spec: Specification<Room>? = null

        if (stationID != null) {
            spec = Specification.where(RoomsSpecifications.hasStationID(stationID))
        }

        if (number != null) {
            spec = (spec ?: Specification.where(null))
                .and(RoomsSpecifications.hasNumber(number))
        }

        return if (spec != null) {
            roomsRepository.findAll(spec, pageable)
        } else {
            roomsRepository.findAll(pageable)
        }
    }


    @Cacheable("rooms")
    fun getById(id: Long): Room? =
        roomsRepository.findById(id)
            .orElse(null)

    @Cacheable("rooms-floor")
    fun findByFloor(floor: Long): List<Room>? =
        roomsRepository.findByfloor(floor)
}