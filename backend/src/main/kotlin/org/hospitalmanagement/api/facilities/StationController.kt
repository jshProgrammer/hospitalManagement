package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.models.classes.facilities.Station
import org.hospitalmanagement.services.facilities.StationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/stations")
class StationController(private val stationService: StationService) {

    @GetMapping
    fun getAll(
        pageable: Pageable
    ): Page<Station> =
        stationService.findAll(pageable)

    /*
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Station =
        stationService.findById(id)
    */
}