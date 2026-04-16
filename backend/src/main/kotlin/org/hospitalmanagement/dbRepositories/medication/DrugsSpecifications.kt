package org.hospitalmanagement.dbRepositories.medication

import org.springframework.data.jpa.domain.Specification
import org.hospitalmanagement.models.classes.medication.Drug
import org.hospitalmanagement.models.enums.DrugsType

object DrugsSpecifications {

    fun hasName(name: String): Specification<Drug> =
        Specification { root, _, cb ->
            cb.equal(root.get<String>("name"), name)
        }

    fun nameContains(name: String): Specification<Drug> =
        Specification { root, _, cb ->
            cb.like(cb.lower(root.get("name")), "%${name.lowercase()}%")
        }

    fun hasActiveIngredient(activeIngredient: String): Specification<Drug> =
        Specification { root, _, cb ->
            cb.equal(root.get<String>("activeIngredient"), activeIngredient)
        }

    fun hasType(type: DrugsType): Specification<Drug> =
        Specification { root, _, cb ->
            cb.equal(root.get<DrugsType>("type"), type)
        }

    // TODO: add criticalAmountInDays-filtering
}