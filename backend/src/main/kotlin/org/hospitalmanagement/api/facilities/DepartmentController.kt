package org.hospitalmanagement.api.facilities

import org.hospitalmanagement.models.classes.facilities.Department
import org.hospitalmanagement.service.facilities.DepartmentService
import org.springframework.data.domain.Page
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
    fun getDepartments(
        pageable: Pageable,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) nameContains: String?,
        @RequestParam(required = false) building: String?
    ): Page<Department> {
        return departmentService.search(
            pageable,
            name,
            nameContains,
            building
        )
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): Department =
        departmentService.getById(id)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Department with id $id not found"
            )

}