package org.hospitalmanagement.services

import org.hospitalmanagement.db.Room
import org.hospitalmanagement.db.repositories.facilities.RoomsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class RoomsService(private val roomsRepository: RoomsRepository) {

    fun getAll(pageable: Pageable): Page<Room> =
        roomsRepository.findAll(pageable)

    fun getById(id: Long): Room? =
        roomsRepository.findById(id)
            .orElse(null)

    fun findByFloor(floor: Long): List<Room>? =
        roomsRepository.findByfloor(floor)

    fun create(room: Room): Room =
        roomsRepository.save(room)
}