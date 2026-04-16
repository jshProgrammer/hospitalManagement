package org.hospitalmanagement.services.facilities

import org.hospitalmanagement.models.classes.facilities.Station
import org.hospitalmanagement.dbRepositories.facilities.StationRepository
import org.hospitalmanagement.dbRepositories.facilities.StationSpecifications
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class StationService(private val stationRepository: StationRepository) {

    fun findById(id: Long): Station? =
        stationRepository.findById(id).orElse(null)

    fun search(
        pageable: Pageable,
        name: String?,
        nameContains: String?,
        departmentId: Long?
    ): Page<Station> {

        if (name != null && nameContains != null) {
            throw IllegalArgumentException("Use either name or nameContains, not both")
        }

        var spec: Specification<Station>? = null

        if (name != null) {
            spec = Specification.where(StationSpecifications.hasName(name))
        }

        if (nameContains != null) {
            spec = (spec ?: Specification.where(null))
                .and(StationSpecifications.nameContains(nameContains))
        }

        if (departmentId != null) {
            spec = (spec ?: Specification.where(null))
                .and(StationSpecifications.hasDepartmentId(departmentId))
        }

        return if (spec != null) {
            stationRepository.findAll(spec, pageable)
        } else {
            stationRepository.findAll(pageable)
        }
    }

}