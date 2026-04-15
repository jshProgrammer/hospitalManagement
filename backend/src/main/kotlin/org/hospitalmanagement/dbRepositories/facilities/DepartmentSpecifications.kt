package org.hospitalmanagement.dbRepositories.facilities

import org.springframework.data.jpa.domain.Specification
import org.hospitalmanagement.models.classes.facilities.Department

object DepartmentSpecifications {

    fun hasName(name: String): Specification<Department> =
        Specification { root, _, cb ->
            cb.equal(root.get<String>("name"), name)
        }

    fun nameContains(name: String): Specification<Department> =
        Specification { root, _, cb ->
            cb.like(cb.lower(root.get("name")), "%${name.lowercase()}%")
        }

    fun hasBuilding(building: String): Specification<Department> =
        Specification { root, _, cb ->
            cb.equal(root.get<String>("building"), building)
        }
}