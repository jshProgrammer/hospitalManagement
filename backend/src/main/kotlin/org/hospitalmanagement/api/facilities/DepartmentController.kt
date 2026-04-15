package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.models.classes.facilities.Department
import org.hospitalmanagement.service.facilities.DepartmentService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/departments")
class DepartmentController(
    private val departmentService: DepartmentService
) {
    @GetMapping
    fun getAll(
        pageable: Pageable,
    ) = departmentService.getAll(pageable)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Department =
        departmentService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Department with id $id not found"
            )

    @GetMapping("/name")
    fun getByName(@RequestParam name: String): Department =
        departmentService.getByName(name)
            ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "Department with name $name not found"
        )

    @GetMapping("/search")
    fun getByNameContainingIgnoreCase(@RequestParam name: String): List<Department> =
        departmentService.getByNameContainingIgnoreCase(name)

    @GetMapping("/building")
    fun getByBuilding(@RequestParam building: String): List<Department> =
        departmentService.getByBuilding(building)
}