package org.hospitalmanagement.dbRepositories.facilities

import org.hospitalmanagement.db.Room
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

@Repository
interface RoomsRepository: JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    override fun findAll(pageable: Pageable): Page<Room>
    fun findByfloor(floor: Long): List<Room>
}