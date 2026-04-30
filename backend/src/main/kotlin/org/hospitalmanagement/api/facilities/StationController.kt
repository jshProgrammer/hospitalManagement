package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.models.classes.facilities.Department
import org.hospitalmanagement.models.classes.facilities.Station
import org.hospitalmanagement.services.facilities.StationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/stations")
class StationController(private val stationService: StationService) {

    @GetMapping
    fun getStations(
        pageable: Pageable,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) nameContains: String?,
        @RequestParam(required = false) departmentId: Long?
    ): Page<Station> {
        return stationService.search(
            pageable,
            name,
            nameContains,
            departmentId
        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Station =
        stationService.findById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Station with id $id not found"
            )
}