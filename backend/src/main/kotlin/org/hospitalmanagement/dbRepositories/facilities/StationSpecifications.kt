package org.hospitalmanagement.dbRepositories.facilities

import org.springframework.data.jpa.domain.Specification
import org.hospitalmanagement.models.classes.facilities.Station

object StationSpecifications {

    fun hasName(name: String): Specification<Station> =
        Specification { root, _, cb ->
            cb.equal(root.get<String>("name"), name)
        }

    fun nameContains(name: String): Specification<Station> =
        Specification { root, _, cb ->
            cb.like(cb.lower(root.get("name")), "%${name.lowercase()}%")
        }

    fun hasDepartmentId(departmentId: Long): Specification<Station> =
        Specification { root, _, cb ->
            cb.equal(root.get<Station>("department").get<Long>("id"), departmentId)
        }
}